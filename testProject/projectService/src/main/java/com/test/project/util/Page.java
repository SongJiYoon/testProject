package com.test.project.util;

/**
 * Created by CHY on 2016-03-31.
 */
public class Page extends BaseModel
{
    private int mAngle;

    private int mPageType;

    private int mBgDisabled;

    private Page()
    {
    }

    public int getAngle()
    {
        return mAngle;
    }

    public int getPageType()
    {
        return mPageType;
    }

    public boolean isBgDisabled()
    {
        return mBgDisabled > 0;
    }

    public static class Builder
    {
        private Page mObj;

        public Builder()
        {
            mObj = new Page();
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

        public Builder pageAttr( int angle, boolean bgDisabled, int pageType )
        {
            mObj.mAngle = angle;
            mObj.mBgDisabled = bgDisabled ? 1 : 0;
            mObj.mPageType = pageType;
            return this;
        }

        public Page build()
        {
            return mObj;
        }
    }
}
