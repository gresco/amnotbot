# Amnotbot

Amnotbot is a simple pure-java IRC bot.

## Acquiring The Source

The master git repository is located at http://github.com/amnotbot/amnotbot
and may be obtained via `git clone git://github.com/amnotbot/amnotbot.git`

## Coding Conventions

To enhance readability and consistency throughout the codebase,
please adhere to the following guidelines. These rules are based
upon the Java coding conventions put in place by Sun which are
available at http://java.sun.com/docs/codeconv/. If something
is not covered in this document please reference the Java conventions
for standard practices.

* Write javadoc for all non-private methods/fields (and private
  ones if you like)

* All indentation shall be done in increments of four spaces. There
  should be no tabs in the source.

* No lines shall be longer than 80 characters unless
  ABSOLUTELY NECESSARY. If an expression will not fit on a single
  line, please follow these general guidelines outlined in the
  Java code conventions:

    * Break after a comma.

    * Break before an operator.

    * Prefer higher-level breaks to lower-level breaks.

    * Align the new line with the beginning of the expression
      at the same level on the previous line.

    * If the above rules lead to confusing code or code that is
      deemed too far to the right margin (use your judgement),
      simply indent 8 spaces.

            /*
             * This is the WRONG way to declare a long method
             */
            private void fooMethod(SomeArg arg1, SomeArg arg2)
            {
                ....
            }

            /* This is the CORRECT way to declare many args */
            private void fooMethod(SomeArg arg1,
                                   AnotherArg arg1)
            {
                ...
            }

* All comments should be done with either C style `/*...*/` or
  Javadoc `/**...*/` comments where appropriate. C++ style `//...`
  comments may be used solely for commenting out unused code.
  Multiline comments should have a an opening line of `/*` and a
  closing line of `*/` with `* ...` as the body as demonstrated below.

        /*
         * This is a proper multi-line comment.
         */

* All class and method declarations shall have their opening and
  closing curly-brace on a new line with the exception of an
  empty method which may be declared on a single line as shown below

        /**
         * An EMPTY method may be declared on a single line
         */
        public void nothingHere() {}

* If anything here appears unclear, or there seems to be some
  standard in the codebase not present in this file, please contact
  the authors and add them to this file. If you have any questions
  about how something should be done, take a look at how existing
  code is performing the task.

## Tests

Once unit testing is implemented in the codebase it will be required
for all new classes to have a complimentary test case to be run at
build time.

## Sharing Your Changes

The best way to share your changes would be to publish a repository
that the authors may "pull" from and integrate it in to the source
tree. Of course, standard patches will be accepted, but may take
longer to become integrated in to the tree once they have been
accepted.
