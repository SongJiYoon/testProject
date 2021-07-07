package com.test.project.util;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by CHY on 2016-04-05.
 */
public class DefaultSaxParser extends DefaultActionParser
{
    public static float PIXEL_TO_DOT_SCALE = 600f / 72f / 56f;

    private int mBook = 0, mSection = 0, mOwner = 0, mStartPage = 0, mTotalPageSize = 0, mSegmentPageSize = 0, mSegmentNumber = 0, mSegmentStartPage = 0, mSegmentEndPage = 0;

    private String mTag = "", mVersion = "";

    private String mSubCode = "";

    private boolean mIsSymbolTag = false;

    private Symbol.Builder mSymbolBuilder = null;

    public DefaultSaxParser( String file )
    {
        super( file );
    }

    public DefaultSaxParser( File file )
    {
        super( file );
    }

    @Override
    public void doParse( final IActionParserResultHandler handler ) throws SAXException, ParserConfigurationException, IOException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        // nproj는 namespace를 정의하지 않으므로 default xml namespace를 enable.
        // java는 default false, android는 default true;
        factory.setNamespaceAware( true );

        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();

        reader.setContentHandler( new DefaultHandler()
        {
            @Override
            public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException
            {
                mTag = localName;

                if ( mTag.equals( "nproj" ) )
                {
                    mVersion = atts.getValue( "version" );
                }
                else if ( mTag.equals( "segment_info" ) )
                {
                    mSubCode = atts.getValue( "sub_code" );
                    mTotalPageSize = Integer.parseInt( atts.getValue( "total_size" ) );
                    mSegmentPageSize = Integer.parseInt( atts.getValue( "size" ) );
                    mSegmentNumber = Integer.parseInt( atts.getValue( "current_sequence" ) );
                    mSegmentStartPage = Integer.parseInt( atts.getValue( "ncode_start_page" ) );
                    mSegmentEndPage = Integer.parseInt( atts.getValue( "ncode_end_page" ) );


                    Segment newSegment = new Segment.Builder()
                            .section( mSection )
                            .owner( mOwner )
                            .book(mBook)
                            .segmentNumber( mSegmentNumber )
                            .subcode( mSubCode )
                            .totalPageSize( mTotalPageSize )
                            .pageSize( mSegmentPageSize )
                            .startPageNumber( mSegmentStartPage )
                            .endPageNumber( mSegmentEndPage )
                            .version( mVersion )
                            .build();

                    handler.onCreateObject( newSegment );
                }
                else if ( mTag.equals( "pages" ) )
                {

                }
                else if ( mTag.equals( "page_item" ) )
                {
                    float x1 = Float.parseFloat( atts.getValue( "x1" ) ) * PIXEL_TO_DOT_SCALE;
                    float x2 = Float.parseFloat( atts.getValue( "x2" ) ) * PIXEL_TO_DOT_SCALE;
                    float y1 = Float.parseFloat( atts.getValue( "y1" ) ) * PIXEL_TO_DOT_SCALE;
                    float y2 = Float.parseFloat( atts.getValue( "y2" ) ) * PIXEL_TO_DOT_SCALE;

                    String crop = atts.getValue( "crop_margin" );
                    String[] crops = crop.split( "," );
                    float margin_left = Float.parseFloat( crops[0] ) * PIXEL_TO_DOT_SCALE;
                    float margin_right = Float.parseFloat( crops[1] ) * PIXEL_TO_DOT_SCALE;
                    float margin_top = Float.parseFloat( crops[2] ) * PIXEL_TO_DOT_SCALE;
                    float margin_bottom = Float.parseFloat( crops[3] ) * PIXEL_TO_DOT_SCALE;

                    Page newPage = new Page.Builder()
                            .version( mVersion )
                            .paper( mSection, mOwner, mBook, Integer.parseInt( atts.getValue( "number" ) ) + mStartPage, mSegmentNumber )
                            .size( margin_left, margin_top, x2 - x1 - margin_left - margin_right, y2 - y1 - margin_top - margin_bottom)
                            .pageAttr( Integer.parseInt( atts.getValue( "rotate_angle" ) ), Boolean.parseBoolean( atts.getValue( "bg_disabled" ) ),Integer.parseInt(atts.getValue( "page_type" ))).build();

                    handler.onCreateObject( newPage );
                }
                else if ( mTag.equals( "symbol" ) )
                {
                    mIsSymbolTag = true;
                    mSymbolBuilder = new Symbol.Builder()
                            .version( mVersion )
                            .paper( mSection, mOwner, mBook, Integer.parseInt( atts.getValue( "page" ) ) + mStartPage, mSegmentNumber )
                            .size( Float.parseFloat( atts.getValue( "x" ) ) * PIXEL_TO_DOT_SCALE, Float.parseFloat( atts.getValue( "y" ) ) * PIXEL_TO_DOT_SCALE, Float.parseFloat( atts.getValue( "width" ) ) * PIXEL_TO_DOT_SCALE, Float.parseFloat( atts.getValue( "height" ) ) * PIXEL_TO_DOT_SCALE );
                }
                else if ( mIsSymbolTag && mTag.equals( "command" ) )
                {
                    mSymbolBuilder.command( atts.getValue( "name" ), atts.getValue( "action" ), atts.getValue( "param" ) );
                }
                else if ( mIsSymbolTag && mTag.equals( "matching_symbols" ) )
                {
                    mSymbolBuilder.siblings( atts.getValue( "previous" ), atts.getValue( "next" ) );
                }
            }

            @Override
            public void endElement( String uri, String localName, String qName ) throws SAXException
            {
                if ( localName.equals( "symbol" ) && mSymbolBuilder != null )
                {
                    handler.onCreateObject( mSymbolBuilder.build() );
                    mIsSymbolTag = false;
                    mSymbolBuilder = null;
                }
            }

            @Override
            public void characters( char[] ch, int start, int length ) throws SAXException
            {
                if ( length <= 0 || mTag == null )
                {
                    return;
                }

                if ( mTag.equals( "owner" ) )
                {
                    mOwner = Integer.parseInt( charsToString( ch, start, length ) );
                    mTag = null;
                }
                else if ( mTag.equals( "code" ) )
                {
                    mBook = Integer.parseInt( charsToString( ch, start, length ) );
                    mTag = null;
                }
                else if ( mTag.equals( "section" ) )
                {
                    mSection = Integer.parseInt( charsToString( ch, start, length ) );
                    mTag = null;
                }
                else if ( mTag.equals( "start_page" ) )
                {
                    mStartPage = Integer.parseInt( charsToString( ch, start, length ) );
                    mTag = null;
                }

                if ( mIsSymbolTag && mTag.equals( "id" ) && mSymbolBuilder != null )
                {
                    mSymbolBuilder.id( charsToString( ch, start, length ) );
                    mTag = null;
                }
            }
        });

        reader.parse( new InputSource( new FileInputStream( mFile ) ) );
    }

    private String charsToString( char[] ch, int start, int length )
    {
        String value = null;

        if ( ch.length > 0 )
        {
            StringBuilder item = new StringBuilder();
            item.append( ch, start, length );

            value = item.toString().trim();

            if ( value.equals( "" ) )
            {
                value = null;
            }

            item = null;
        }

        return value;
    }
}
