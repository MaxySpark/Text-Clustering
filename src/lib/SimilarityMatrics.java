package lib;


public class SimilarityMatrics {
      // Cosine Similarity
        public static float FindCosineSimilarity(float[] vecA, float[] vecB)
        {
            float dotProduct = DotProduct(vecA, vecB);
            float magnitudeOfA = Magnitude(vecA);
            float magnitudeOfB = Magnitude(vecB);
            
            if(magnitudeOfA == 0 || magnitudeOfB == 0) {
                return 0;
            } else {
                float result = dotProduct / (magnitudeOfA * magnitudeOfB);
                return (float)result;
            }
        }


        public static float DotProduct(float[] vecA, float[] vecB)
        {

            float dotProduct = 0;
            for (int i = 0; i < vecA.length; i++)
            {
                dotProduct += (vecA[i] * vecB[i]);
            }

            return dotProduct;
        }

        // Magnitude of the vector is the square root of the dot product of the vector with itself.
        public static float Magnitude(float[] vector)
        {
            return (float)Math.sqrt(DotProduct(vector, vector));
        }



//        //Euclidean Distance
//        //Computes the similarity between two documents as the distance between their point representations. Is translation invariant.
//        public static float FindEuclideanDistance(int[] vecA, int[] vecB)
//        {
//            float euclideanDistance = 0;
//            for (int i = 0; i < vecA.length; i++)
//            {
//                euclideanDistance += (float)Math.pow((vecA[i] - vecB[i]), 2);
//            }
//
//            return (float)Math.sqrt(euclideanDistance);
//
//        }
//
//        //region Extended Jaccard
//        //Combines properties of both cosine similarity and Euclidean distance
//        public static float FindExtendedJaccard(float[] vecA, float[] vecB)
//        {
//            float dotProduct = DotProduct(vecA, vecB);
//            float magnitudeOfA = Magnitude(vecA);
//            float magnitudeOfB = Magnitude(vecB);
//
//            return dotProduct / (magnitudeOfA + magnitudeOfB - dotProduct);
//
//        }
}
