package org.knix.amnotbot.cmd;

import org.knix.amnotbot.cmd.utils.CmdOptionImp;
import org.knix.amnotbot.cmd.utils.CommandOptions;
import org.knix.amnotbot.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import javax.naming.directory.InvalidAttributeValueException;
import org.knix.amnotbot.config.BotConfiguration;

public class WordsCommandThread extends Thread
{
    
    BotMessage msg;
    CommandOptions opts;    

    public enum countOperation {
        WORDS, LINES
    }
    countOperation countOp;

    public WordsCommandThread(BotMessage msg, countOperation op)
    {
        this.msg = msg;
        this.countOp = op;

        opts = new CommandOptions(msg.getText());

        opts.addOption(new CmdOptionImp("nick", ","));
        opts.addOption(new CmdOptionImp("word", ","));
        opts.addOption(new CmdOptionImp("number"));
        opts.addOption(new CmdOptionImp("date"));
        opts.addOption(new CmdOptionImp("op"));
        opts.addOption(new CmdOptionImp("channel"));

        start();
    }

    public void run()
    {
        this.init();

        String db_file = null;
        try {
            db_file = this.selectDBFile();
        } catch (Exception e) {
            BotLogger.getDebugLogger().debug(e.getMessage());
            e.printStackTrace();
            return;
        }
        
        this.processRequest( this.selectBackend(db_file) );
    }

    private void init()
    {
        this.opts.buildArgs();
    }

    private String selectDBFile()
            throws FileNotFoundException, InvalidAttributeValueException
    {
        String target = this.msg.getTarget();
        if (this.opts.getOption("channel").hasValue()) {
            target = this.opts.getOption("channel").tokens()[0];
        }

        if (target.charAt(0) != '#') {
            throw new InvalidAttributeValueException("Not a valid channel: " +
                    target + "). Use the 'channel:' option.");
        }

        String db_file = this.msg.getConn().getBotLogger().getLoggingPath() +
                "/" + target;
        if (!this.dbExists(db_file)) {
            throw new FileNotFoundException("Statistics not available for: " +
                    target);
        }
        return db_file;
    }

    boolean dbExists(String path)
    {
        File db_file = new File(path);

        if (!db_file.exists()) return false;

        return true;
    }

    private WordCounter selectBackend(String db_file)
    {
        String wCounter;
        wCounter = BotConfiguration.getConfig().getString("word_counter_imp");
        if (wCounter.compareTo("sqlite") == 0) {
            return ( new WordCounterSqlite(db_file) );
        }

        String i_file;
        i_file = BotConfiguration.getConfig().getString("ignored_words_file");
        return (new WordCounterTextFile(i_file, db_file) );
    }

    private void processRequest(WordCounter wordCounter)
    {
        String num;
        String [] nicks;

        num = this.opts.getOption("number").tokens()[0];
        int n = num == null ? 5 : Integer.parseInt(num);
        nicks = this.opts.getOption("nick").tokens();

        WordResults results = new WordResults();
        switch (this.countOp) {
            case WORDS:
                results = this.countWords(wordCounter, n, nicks);
                break;
            case LINES:
                results = this.countLines(wordCounter, n, nicks);
                break;
        }
        this.showResults(results);
    }

    private WordResults countWords(WordCounter wordCounter,
            int n,
            String [] nicks)
    {
        String words;
        WordResults results = new WordResults();
        if (this.opts.getOption("word").hasValue()) {
            words = wordCounter.mostUsedWordsBy(n,
                    this.opts.getOption("word").tokens(),
                    this.opts.getOption("date").tokens()[0]);
        } else {
            words = wordCounter.mostUsedWords(n, nicks,
                    this.opts.getOption("date").tokens()[0]);
        }
        results.setOutputMessage("Most used words for ");
        results.setWords(words);
        return results;
    }

    private WordResults countLines(WordCounter wordCounter,
            int n,
            String [] nicks)
    {
        String op = "";
        if (this.opts.getOption("op").hasValue()) {
            op = this.opts.getOption("op").tokens()[0];
        }

        String words;
        WordResults results = new WordResults();
        if (op.compareTo("avg") == 0) {
            words = wordCounter.avgWordsLine(n, nicks,
                    this.opts.getOption("date").tokens()[0]);
            results.setOutputMessage("Avg. words per line per user for ");
        } else {
            words = wordCounter.topLines(n,
                    this.opts.getOption("date").tokens()[0]);
            results.setOutputMessage("Lines per user for ");
        }
        results.setWords(words);
        
        return results;
    }

    private void showResults(WordResults results)
    {
        String target;
        BotConnection conn;

        conn = this.msg.getConn();
        target = this.msg.getTarget();
        if (!results.hasResults()) {
            conn.doPrivmsg(target, "Could not find any match!");
            return;
        }

        LinkedList<String> output = this.truncateOutput(results);
        if (opts.getOption("nick").hasValue()) {
            conn.doPrivmsg(target, results.getOutputMessage() + "'" +
                    opts.getOption("nick").tokens()[0] +
                    "': " + output.getFirst());
        } else {
            conn.doPrivmsg(target, results.getOutputMessage() + "'" +
                    target + "': " + output.getFirst());
        }

        for (int j = 1; j < output.size(); ++j) {
            conn.doPrivmsg(target, output.get(j));
            try {
                // avoid being disconnected by flooding
                Thread.sleep(300 * j);
            } catch (InterruptedException e) {
                BotLogger.getDebugLogger().debug(e.getMessage());
                break;
            }
        }
    }

    private LinkedList<String> truncateOutput(WordResults results)
    {
        LinkedList<String> wList = new LinkedList<String>();
        // irc client truncates everything over 440 chars
        int position = 0, maxChars = 430;
        String words = results.getWords();
        int wordsLength = results.getWords().length();
        int msgLength = results.getOutputMessage().length();
        while ((wordsLength + msgLength) - position > maxChars) {
            int truncPosition;
            truncPosition = words.indexOf(' ', (maxChars / 2) + position);
            wList.add(words.substring(position, truncPosition));
            position = truncPosition;
        }
        wList.add(words.substring(position, wordsLength));        
        return wList;
    }

    private class WordResults
    {
        private String words;
        private String outputMessage;

        WordResults() 
        {
            this.words = null;
            this.outputMessage = null;
        }
                
        WordResults(String words, String outputMessage)
        {
            this.words = words;
            this.outputMessage = outputMessage;
        }

        public boolean hasResults()
        {
            return (this.words != null);
        }

        public String getWords()
        {
            return this.words;
        }

        public String getOutputMessage()
        {
            return this.outputMessage;
        }

        public void setWords(String words)
        {
            this.words = words;
        }

        public void setOutputMessage(String msg)
        {
            this.outputMessage = msg;
        }
    }
}