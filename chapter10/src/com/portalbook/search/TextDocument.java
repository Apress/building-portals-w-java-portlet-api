package com.portalbook.search;

import java.io.*;

import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class TextDocument
{
    public static Document createDocument(File file)
    {
        Document doc = new Document();

        // create an unindexed, untokenized, stored field for the path
        doc.add(Field.UnIndexed("path", file.getPath()));

        // create an indexed, untokenized, stored field for the last modified time
        // use DateField to convert time to a String
        doc.add(
            Field.Keyword(
                "lastModified",
                DateField.timeToString(file.lastModified())));

        // create an unindexed, untokenized, stored field for the size
        doc.add(Field.UnIndexed("size", Long.toString(file.length())));

        // create an indexed, tokenized, stored field for the short description
        FileReader reader;
        try
        {
            reader = new FileReader(file);
			doc.add(Field.Text("content", reader));
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

		return doc;
    }
}
