package lib;

import java.util.*;
import java.util.regex.*;

public class VectorSpaceModel {
        private static HashSet<String> distinctTerms;
        private static ArrayList<String> documentCollection;
        private static Pattern ptn = Pattern.compile("([ \\t{}()\",:;. \n])");

      /// <summary>
      /// Prepares a collection of document in vector space
      /// </summary>
      /// <param name="collection">Document collection/corpus</param>
      /// <returns>List of, document in vector space</returns>
        public static ArrayList<DocumentVector> ProcessDocumentCollection(DocumentCollection collection)
        {

            distinctTerms = new HashSet<String>();
            documentCollection = collection.DocumentList;

           /*
            * Finds out the total no of distinct terms in the whole corpus so that it will be easy
            * to represent the document in the vector space. The dimension of the vector space will
            * be equal to the total no of distinct terms.
            *
            */

            for (String documentContent : collection.DocumentList)
            {
                for (String term : documentContent.split(ptn.pattern()))
                {
                    if (!StopWordsHandler.IsStotpWord(term))
                        distinctTerms.add(term);
                    else
                        continue;
                }
            }
            
            String[] rmvArr = new String[]{"\"","\r","\n","(",")","[","]","{","}","","."," ",","};
            ArrayList<String> removeList = new ArrayList<String>(Arrays.asList(rmvArr));
            for (String s : removeList)
            {
                distinctTerms.remove(s);
            }

            //
            //CURRENT PROGRESS
            //
                
            ArrayList<DocumentVector> documentVectorSpace = new ArrayList<DocumentVector>();
            DocumentVector _documentVector;
            float[] space;
            for (String document : documentCollection)
            {
                int count = 0;
                space = new float[distinctTerms.size()];
                for (String term : distinctTerms)
                {
                    space[count] = FindTFIDF(document,term);
                    count++;
                }
                _documentVector = new DocumentVector();
                _documentVector.Content = document;
                _documentVector.VectorSpace = space;
                documentVectorSpace.add(_documentVector);

            }

            return documentVectorSpace;

        }
        //Calculate TF-IDF

        //Calculates TF-IDF weight for each term t in document d
        private static float FindTFIDF(String document, String term)
        {
            float tf = FindTermFrequency(document, term);
            float idf = FindInverseDocumentFrequency(term);
            return tf * idf;
        }

        private static float FindTermFrequency(String document, String term)
        {

            String[] splitArr = document.split(ptn.pattern());
            
            int count = Arrays.stream(splitArr)
                    .filter(s -> s.toUpperCase().equals(term.toUpperCase()))
                    .toArray(String[]::new)
                    .length
                    ;
            //ratio of no of occurance of term t in document d to the total no of terms in the document
            return (float)((float)count / (float)(document.split(ptn.pattern()).length));
        }


        private static float FindInverseDocumentFrequency(String term)
        {
            //find the  no. of document that contains the term in whole document collection
            
            String[] docArr = documentCollection.toArray(new String[documentCollection.size()]);
            
            int count = Arrays.stream(docArr)
                    .filter(
//                            s => r.Split(s.ToUpper()).ToArray().Contains(term.ToUpper())
                            s -> Arrays.asList(s.toUpperCase().split(ptn.pattern())).contains(term.toUpperCase())

                    ).toArray(String[]::new)
                    .length;
            /*
             * log of the ratio of  total no of document in the collection to the no. of document containing the term
             * we can also use Math.Log(count/(1+documentCollection.Count)) to deal with divide by zero case;
             */
            return (float)Math.log((float)documentCollection.size() / (float)count);

        }
}
