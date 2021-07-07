package com.test.project.util;

import java.io.File;

/**
 * Created by CHY on 2016-04-06.
 */
public abstract class DefaultActionParser implements IActionParser
{
    protected File mFile;

    public DefaultActionParser( File file )
    {
        mFile = file;
    }

    public DefaultActionParser( String filepath )
    {
        this(new File(filepath));
    }

    @Override
    public abstract void doParse( IActionParserResultHandler handler ) throws Exception;
}
