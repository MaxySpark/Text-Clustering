package lib;

import java.util.*;
public class DocumentClustering {
    private static int globalCounter = 0;
    private static int counter;


    public static WrapPC PrepareDocumentCluster(int k, ArrayList<DocumentVector> documentCollection,int _counter)
//    public static ArrayList<Centroid> PrepareDocumentCluster(int k, ArrayList<DocumentVector> documentCollection,int _counter)

    {
        globalCounter = 0;
        
        WrapPC tempW = new WrapPC();
        
        //prepares k initial centroid and assign one object randomly to each centroid
        ArrayList<Centroid> centroidCollection = new ArrayList<Centroid>();
        Centroid c;

        /*
         * Avoid repeation of random number, if same no is generated more than once same document is added to the next cluster 
         * so avoid it using HasSet collection
         */
        HashSet<Integer> uniqRand = new HashSet<Integer>();
        uniqRand = GenerateRandomNumber(uniqRand,k,documentCollection.size());

        for (int pos : uniqRand) 
        {
            c = new Centroid();                
            c.GroupedDocument = new ArrayList<DocumentVector>();
            c.GroupedDocument.add(documentCollection.get(pos));
            centroidCollection.add(c);                
        }

        Boolean stoppingCriteria;
        ArrayList<Centroid> resultSet = null;
        ArrayList<Centroid> prevClusterCenter;

        resultSet = InitializeClusterCentroid(resultSet, centroidCollection.size());

        do
        {
            prevClusterCenter = centroidCollection;

            for (DocumentVector obj : documentCollection)
            {
                int index = FindClosestClusterCenter(centroidCollection, obj);
                resultSet.get(index).GroupedDocument.add(obj);
            }
            centroidCollection = InitializeClusterCentroid(centroidCollection, centroidCollection.size());
            centroidCollection = CalculateMeanPoints(resultSet);
            stoppingCriteria = CheckStoppingCriteria(prevClusterCenter, centroidCollection);
            if (!stoppingCriteria)
            {   
                //initialize the result set for next iteration
                resultSet = InitializeClusterCentroid(resultSet, centroidCollection.size());
            }


        } while (stoppingCriteria == false);

//        _counter = counter;
//        return resultSet;
        
        tempW.counter = counter;
        tempW.resultSet = resultSet;
        
        return tempW;

    }

    /// <summary>
    /// Generates unique random numbers and also ensures the generated random number 
    /// lies with in a range of total no. of document
    /// </summary>
    /// <param name="uniqRand"></param>
    /// <param name="k"></param>
    /// <param name="docCount"></param>

    private static HashSet<Integer> GenerateRandomNumber(HashSet<Integer> uniqRand, int k, int docCount)
    {

        Random r = new Random();

        if (k > docCount)
        {
            do
            {
                int pos = r.nextInt(docCount);
                uniqRand.add(pos);

            } while (uniqRand.size() != docCount);
        }            
        else
        {
            do
            {
                int pos = r.nextInt(docCount);
                uniqRand.add(pos);

            } while (uniqRand.size() != k);
        }
        return uniqRand;
    }

    /// <summary>
    /// Initialize the result cluster centroid for the next iteration, that holds the result to be returned
    /// </summary>
    /// <param name="centroid"></param>
    /// <param name="count"></param>
    private static ArrayList<Centroid> InitializeClusterCentroid(ArrayList<Centroid> centroid,int count)
    {
        Centroid c;
        centroid = new ArrayList<Centroid>();
        for (int i = 0; i < count; i++)
        {
            c = new Centroid();
            c.GroupedDocument = new ArrayList<DocumentVector>();
            centroid.add(c);
        }
        
        return centroid;

    }

    /// <summary>
    /// Check the stopping criteria for the iteration, if centroid do not move their position it meets the criteria
    /// or if the global counter exist its predefined limit(minimum iteration threshold) than iteration terminates
    /// </summary>
    /// <param name="prevClusterCenter"></param>
    /// <param name="newClusterCenter"></param>
    /// <returns></returns>
    private static Boolean CheckStoppingCriteria(ArrayList<Centroid> prevClusterCenter, ArrayList<Centroid> newClusterCenter)
    {

        globalCounter++;
        counter = globalCounter;
        if (globalCounter > 11000)
        {
            return true;
        }

        else
        {
            Boolean stoppingCriteria;
            int[] changeIndex = new int[newClusterCenter.size()]; //1 = centroid has moved 0 == centroid do not moved its position

            int index = 0;
            do
            {
                int count = 0;
                if (newClusterCenter.get(index).GroupedDocument.size() == 0 && prevClusterCenter.get(index).GroupedDocument.size() == 0)
                {
                    index++;
                }
                else if (newClusterCenter.get(index).GroupedDocument.size() != 0 && prevClusterCenter.get(index).GroupedDocument.size() != 0)
                {
                    for (int j = 0; j < newClusterCenter.get(index).GroupedDocument.get(0).VectorSpace.length; j++)
                    {
                        //
                        if (newClusterCenter.get(index).GroupedDocument.get(0).VectorSpace[j] == prevClusterCenter.get(index).GroupedDocument.get(0).VectorSpace[j])
                        {
                            count++;
                        }

                    }

                    if (count == newClusterCenter.get(index).GroupedDocument.get(0).VectorSpace.length)
                    {
                        changeIndex[index] = 0;
                    }
                    else
                    {
                        changeIndex[index] = 1;
                    }
                    index++;
                }
                else
                {
                    index++;
                    continue;

                }


            } while (index < newClusterCenter.size());

            // if index list contains 1 stopping criteria is set to flase
            
            // CHANGE IT
            
            if (Arrays.stream(changeIndex).filter(s-> (s!=0 && s==1)).toArray().length > 0)
            
            {
                stoppingCriteria = false;
            }
            else
                stoppingCriteria = true;

            return stoppingCriteria;
        }


    }

    //returns index of closest cluster centroid
    private static int FindClosestClusterCenter(ArrayList<Centroid> clusterCenter,DocumentVector obj)
    {

        float[] similarityMeasure = new float[clusterCenter.size()];

        for (int i = 0; i < clusterCenter.size(); i++)
        {

            similarityMeasure[i] = SimilarityMatrics.FindCosineSimilarity(clusterCenter.get(i).GroupedDocument.get(0).VectorSpace, obj.VectorSpace); 

        }

        int index = 0;
        float maxValue = similarityMeasure[0];
        for (int i = 0; i < similarityMeasure.length; i++)
        {
            //if document is similar assign the document to the lowest index cluster center to avoid the long loop
            if (similarityMeasure[i] >maxValue)
            {
                maxValue = similarityMeasure[i];
                index = i;

            }
        }
        return index;

    }

    //Reposition the centroid
    private static ArrayList<Centroid> CalculateMeanPoints(ArrayList<Centroid> _clusterCenter)
    {

        for (int i = 0; i < _clusterCenter.size(); i++)
        {

            if (_clusterCenter.get(i).GroupedDocument.size() > 0)
            {

                for (int j = 0; j < _clusterCenter.get(i).GroupedDocument.get(0).VectorSpace.length; j++)
                {
                    float total = 0;

                    for (DocumentVector vSpace : _clusterCenter.get(i).GroupedDocument)
                    {

                        total += vSpace.VectorSpace[j];

                    }

                    //reassign new calculated mean on each cluster center, It indicates the reposition of centroid
                    _clusterCenter.get(i).GroupedDocument.get(0).VectorSpace[j] = total / _clusterCenter.get(i).GroupedDocument.size();

                }

            }

        }

        return _clusterCenter;

    }
    /// <summary>
    /// Find Residual sum of squares it measures how well a cluster centroid represents the member of their cluster
    /// We can use the RSS value as stopping criteria of k-means algorithm when decreses in RSS value falls below a 
    /// threshold t for small t we can terminate the algorithm.
    /// </summary>
    private static void FindRSS(ArrayList<Centroid> newCentroid, ArrayList<Centroid> _clusterCenter) 
    {
        //TODO:
    }
}
