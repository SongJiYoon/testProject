package com.test.project.util;
/**
 * Created by CHY on 2016-03-16.
 */
public class Symbol extends BaseModel
{
    private String mSymbolName;

    private String mSymbolId;

    private String mPreviousId;

    private String mNextId;

    private String mCmdName;

    private String mCmdAction;

    private String mCmdParam;

    private Symbol()
    {
    }

    public String getSymbolName()
    {
        return mSymbolName;
    }

    public String getSymbolId()
    {
        return mSymbolId;
    }

    public String getPreviousId()
    {
        return mPreviousId;
    }

    public String getNextId()
    {
        return mNextId;
    }

    public String getCmdName()
    {
        return mCmdName;
    }

    public String getCmdAction()
    {
        return mCmdAction;
    }

    public String getCmdParam()
    {
        return mCmdParam;
    }

    public static class Builder
    {
        private Symbol mObj;

        public Builder()
        {
            mObj = new Symbol();
        }

        public Builder name( String name )
        {
            mObj.mSymbolName = name;
            return this;
        }

        public Builder version( String mVersion )
        {
            mObj.mVersion = mVersion;
            return this;
        }

        public Builder paper( int section, int owner, int book, int page, int segment )
        {
            mObj.mSection = section;
            mObj.mOwner = owner;
            mObj.mBook = book;
            mObj.mSegmentNumber = segment;
            mObj.mPageNumber = page;
            return this;
        }

        public Builder size( float x, float y, float width, float height )
        {
            mObj.mX = x;
            mObj.mY = y;
            mObj.mWidth = width;
            mObj.mHeight = height;
            return this;
        }

        public Builder id( String id )
        {
            mObj.mSymbolId = id;
            return this;
        }

        public Builder siblings( String previousId, String nextId )
        {
            mObj.mPreviousId = previousId;
            mObj.mNextId = nextId;
            return this;
        }

        public Builder command( String name, String action, String param )
        {
            mObj.mCmdName = name;
            mObj.mCmdAction = action;
            mObj.mCmdParam = param;
            return this;
        }

        public Symbol build()
        {
            return mObj;
        }
    }
}
