package org.commonjava.web.config.io;

import static org.apache.commons.io.FileUtils.readLines;
import static org.apache.commons.lang.StringUtils.join;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ConfigFileUtils
{

    private static final String LS = System.getProperty( "line.separator", "\n" );

    private static final String INCLUDE_COMMAND = "Include ";

    private static final String GLOB_BASE_PATTERN = "([^\\?\\*]+)[\\\\\\/]([\\*\\?]+.+)";

    private static final String GLOB_IDENTIFYING_PATTERN = ".*[\\?\\*].*";

    private ConfigFileUtils()
    {
    }

    public static InputStream readFileWithIncludes( final String path )
        throws IOException
    {
        return readFileWithIncludes( new File( path ) );
    }

    public static InputStream readFileWithIncludes( final File f )
        throws IOException
    {
        final List<String> lines = readLinesWithIncludes( f );

        return new ByteArrayInputStream( join( lines, LS ).getBytes() );
    }

    public static List<String> readLinesWithIncludes( final File f )
        throws IOException
    {
        final List<String> result = new ArrayList<String>();

        final List<String> lines = readLines( f );
        final File dir = f.getParentFile();
        for ( final String line : lines )
        {
            if ( line.startsWith( INCLUDE_COMMAND ) )
            {
                final String glob = line.substring( INCLUDE_COMMAND.length() );
                for ( final File file : findMatching( dir, glob ) )
                {
                    result.addAll( readLinesWithIncludes( file ) );
                }
            }
            else
            {
                result.add( line );
            }
        }

        return result;
    }

    public static File[] findMatching( final File dir, final String glob )
        throws IOException
    {
        if ( !glob.matches( GLOB_IDENTIFYING_PATTERN ) )
        {
            File f = new File( glob );
            if ( !f.isAbsolute() )
            {
                f = new File( dir, glob );
            }

            return new File[] { f };
        }

        final Matcher m = Pattern.compile( GLOB_BASE_PATTERN )
                                 .matcher( glob );
        String base = null;
        String pattern = null;
        if ( m.matches() )
        {
            base = m.group( 1 );
            pattern = m.group( 2 );

            if ( !new File( base ).isAbsolute() )
            {
                base = new File( dir, base ).getAbsolutePath();
            }
        }
        else
        {
            base = dir.getAbsolutePath();
            pattern = glob;
        }

        if ( pattern.length() < 1 )
        {
            return new File[] { new File( base ).getCanonicalFile() };
        }

        final StringBuilder regex = new StringBuilder();
        for ( int i = 0; i < pattern.length(); i++ )
        {
            final char c = pattern.charAt( i );
            switch ( c )
            {
                case '*':
                {
                    if ( i + 1 < pattern.length() && pattern.charAt( i + 1 ) == '*' )
                    {
                        regex.append( ".+" );
                        i++;
                    }
                    else
                    {
                        regex.append( "[^\\\\\\/]*" );
                    }
                    break;
                }
                case '.':
                {
                    regex.append( "\\." );
                    break;
                }
                case '?':
                {
                    regex.append( "." );
                    break;
                }
                default:
                {
                    regex.append( c );
                }
            }
        }

        final boolean dirsOnly = pattern.endsWith( "/" ) || pattern.endsWith( "\\" );
        final String globRegex = regex.toString();
        final File bdir = new File( base ).getCanonicalFile();
        final int bdirLen = bdir.getPath()
                                .length() + 1;

        final List<File> allFiles = listRecursively( bdir );
        for ( final Iterator<File> it = allFiles.iterator(); it.hasNext(); )
        {
            final File f = it.next();
            if ( dirsOnly && !f.isDirectory() )
            {
                it.remove();
                continue;
            }

            final String sub = f.getAbsolutePath()
                                .substring( bdirLen );

            if ( !sub.matches( globRegex ) )
            {
                it.remove();
            }
        }

        return allFiles.toArray( new File[] {} );
    }

    private static List<File> listRecursively( final File dir )
        throws IOException
    {
        final List<File> files = new ArrayList<File>();
        final File d = dir.getCanonicalFile();

        recurse( d, files );

        return files;
    }

    private static void recurse( final File dir, final List<File> files )
        throws IOException
    {
        if ( !dir.isDirectory() )
        {
            return;
        }

        for ( final String name : dir.list() )
        {
            final File f = new File( dir, name ).getCanonicalFile();

            files.add( f );
            if ( f.isDirectory() )
            {
                recurse( f, files );
            }
        }
    }

}
