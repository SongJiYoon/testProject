package com.test.project.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by CHY on 2016-04-05.
 */
public abstract class BaseModel
{
    protected String mVersion;

    protected int mSection;

    protected int mOwner;

    protected int mBook;

    protected int mSegmentNumber;

    protected int mPageNumber;

    protected float mX;

    protected float mY;

    protected float mWidth;

    protected float mHeight;

    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        try
        {
            List<Field> fields = new ArrayList<Field>( Arrays.<Field>asList( getClass().getSuperclass().getDeclaredFields() ));
            fields.addAll(Arrays.<Field>asList(getClass().getDeclaredFields()));

            for ( Field f : fields )
            {
                f.setAccessible( true );
                String fName = f.getName();
                sb.append(fName);
                sb.append(" : ");
                sb.append(f.get(this));
                sb.append(", ");
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return sb.toString();
    }
    public String getVersion()
    {
        return mVersion;
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

    public int getPageNumber()
    {
        return mPageNumber;
    }

    public float getX()
    {
        return mX;
    }

    public float getY()
    {
        return mY;
    }

    public float getWidth()
    {
        return mWidth;
    }

    public float getHeight()
    {
        return mHeight;
    }

    public float getLeft()
    {
        return mX;
    }

    public float getTop()
    {
        return mY;
    }

    public float getRight()
    {
        return mX + mWidth;
    }

    public float getBottom()
    {
        return mY + mHeight;
    }
}
