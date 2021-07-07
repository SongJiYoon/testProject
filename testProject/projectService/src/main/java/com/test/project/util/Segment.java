package com.test.project.util;
/**
 * Created by CHY on 2016-03-16.
 */
public class Segment
{
    protected String mVersion;

    protected int mSection;

    protected int mOwner;

    protected int mBook;

    protected String mSubCode;

    protected int mSegmentNumber;

    protected int mStartPageNumber;

    protected int mEndPageNumber;

    protected int mTotalPageSize;

    protected int mPageSize;

    private Segment()
    {
    }

    public int getSection()
    {
        return mSection;
    }

    public int getOwner()
    {
        return mOwner;
    }

    public int getBook()
    {
        return mBook;
    }

    public int getSegmentNumber()
    {
        return mSegmentNumber;
    }

    public String getSubCode()
    {
        return mSubCode;
    }

    public int getTotalPageSize()
    {
        return mTotalPageSize;
    }

    public int getStartPageNumber()
    {
        return mStartPageNumber;
    }

    public String getStartPaperId()
    {
        return String.format( "%04d.%05d.%08d.%04d", mSection, mOwner, mBook, mStartPageNumber );
    }

    public int getEndPageNumber()
    {
        return mEndPageNumber;
    }

    public String getEndPaperId()
    {
        return String.format( "%04d.%05d.%08d.%04d", mSection, mOwner, mBook, mEndPageNumber );
    }

    public int getTotalSegmentSize()
    {
        return (int) Math.ceil(mTotalPageSize / (double)mPageSize );
    }

    public int getPageSize()
    {
        return mPageSize;
    }

    public String getVersion()
    {
        return mVersion;
    }

    public static class Builder
    {
        private Segment mObj;

        public Builder()
        {
            mObj = new Segment();
        }

        public Builder section( int section )
        {
            mObj.mSection = section;
            return this;
        }

        public Builder owner( int owner )
        {
            mObj.mOwner = owner;
            return this;
        }

        public Builder book( int book )
        {
            mObj.mBook = book;
            return this;
        }

        public Builder subcode( String subcode )
        {
            mObj.mSubCode = subcode;
            return this;
        }

        public Builder segmentNumber( int segmentNumber )
        {
            mObj.mSegmentNumber = segmentNumber;
            return this;
        }

        public Builder startPageNumber( int startPageNumber )
        {
            mObj.mStartPageNumber = startPageNumber;
            return this;
        }

        public Builder endPageNumber( int endPageNumber )
        {
            mObj.mEndPageNumber = endPageNumber;
            return this;
        }

        public Builder totalPageSize( int totalPageSize )
        {
            mObj.mTotalPageSize = totalPageSize;
            return this;
        }

        public Builder pageSize( int pageSize )
        {
            mObj.mPageSize = pageSize;
            return this;
        }

        public Builder version( String version )
        {
            mObj.mVersion = version;
            return this;
        }

        public Segment build()
        {
            return mObj;
        }
    }
}
