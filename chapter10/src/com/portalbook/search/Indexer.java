package com.portalbook.search;

import java.io.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.demo.HTMLDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

public class Indexer
{

    protected IndexWriter writer = null;

    protected Analyzer analyzer = new StandardAnalyzer();

    public void init(String indexPath) throws IOException
    {
        //set Lucene lockdir
        System.setProperty("org.apache.lucene.lockdir", indexPath);

        //create a new Lucene index, overwriting the existing one.
        writer = new IndexWriter(indexPath, analyzer, true);
    }

    public void indexFiles(String contentPath) throws IOException
    {
        File contentDir = new File(contentPath);

        if (!contentDir.exists())
        {
            throw new IOException("Content directory does not exist.");
        }

        if (!contentDir.isDirectory())
        {
            System.out.println(contentPath + " is not a directory.");
            return;
        }

        File[] indexableFiles = contentDir.listFiles();
        {
            if (indexableFiles != null)
            {
                for (int ctr = 0; ctr < indexableFiles.length; ctr++)
                {
                    if (indexableFiles[ctr].isFile())
                    {
                        updateIndex(writer, indexableFiles[ctr]);
                    }
                }
            }
        }

        //optimize the index
        writer.optimize();

        //close the index
        writer.close();

    }

    public void updateIndex(IndexWriter writer, File file)
    {

        // add the document to the index
        try
        {
            Document doc = HTMLDocument.Document(file);

            writer.addDocument(doc);

        }
        catch (IOException e)
        {
            System.out.println("Error adding document: " + e.getMessage());
        }
        catch (InterruptedException e)
        {
            System.out.println("Error adding document: " + e.getMessage());
        }
    }

    public static void main(String args[])
    {

        Indexer indexer = new Indexer();
        try
        {
            String content = "./content";
            String index = "./lucene";

            if (args.length > 0)
            {
                content = args[0];
                System.out.println(content);
            }

            if (args.length > 1)
            {
                index = args[1];
                System.out.println(index);
            }

            //create the directory for the index if it does not exist
            File indexDir = new File(index);
            indexDir.mkdir();

            indexer.init(index);
            indexer.indexFiles(content);

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
