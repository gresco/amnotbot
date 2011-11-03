/*
 * Copyright (c) 2011 Geronimo Poppino <gresco@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.amnotbot.cmd;

import com.github.amnotbot.cmd.utils.BotURLConnection;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author gpoppino
 */
public class GoogleSearch
{

    public enum searchType {
        BOOKS_SEARCH, NEWS_SEARCH, WEB_SEARCH, PATENT_SEARCH, BLOGS_SEARCH,
        VIDEOS_SEARCH
    }

    private final String SEARCH_URL =
            "http://ajax.googleapis.com/ajax/services/search/";

    public GoogleSearch()
    {
    }

    public JSONObject search(searchType sType, String query)
            throws MalformedURLException, IOException, JSONException
    {
        URL searchUrl;
        searchUrl = this.buildGoogleSearchUrl(sType, query);

        BotURLConnection conn = new BotURLConnection(searchUrl);

        return ( new JSONObject( conn.fetchURL() ) );
    }

    private URL buildGoogleSearchUrl(searchType sType, String query)
            throws MalformedURLException, UnsupportedEncodingException 
    {
        String url = null;

        switch (sType) {
            case WEB_SEARCH:
                url = this.SEARCH_URL + "web?v=1.0&q=";
                break;
            case BOOKS_SEARCH:
                url = this.SEARCH_URL + "books?v=1.0&q=";
                break;
            case NEWS_SEARCH:
                url = this.SEARCH_URL + "news?v=1.0&q=";
                break;
            case PATENT_SEARCH:
                url = this.SEARCH_URL + "patent?v=1.0&q=";
                break;
            case BLOGS_SEARCH:
                url = this.SEARCH_URL + "blogs?v=1.0&q=";
                break;
            case VIDEOS_SEARCH:
                url = this.SEARCH_URL + "video?v=1.0&q=";
                break;
        }

        url += URLEncoder.encode(query, "UTF-8");

        return (new URL(url));
    }
}
