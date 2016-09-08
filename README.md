Apriori

This project demonstrates the Apriori algorithm.

The program generates:
* Frequent itemsets using Fk-1*F1 and Fk-1*Fk-1 methods.
* Frequent Closed Itemsets
* Maximal Frequent Itemsets
* Association Rules
* Enumeration of rules from Confidence-based pruning

Some datasets which can be used are:
1. Car: 1728 records, 7 attributes - https://archive.ics.uci.edu/ml/datasets/Car+Evaluation
2. Mushroom: 8124 records, 23 attributes - https://archive.ics.uci.edu/ml/datasets/Mushroom
3. Nursery: 12960 records, 9 attributes - https://archive.ics.uci.edu/ml/datasets/Nursery

These data sets are converted into a sparse binary matrix using 'binarizer.awk', by running the command:
`$ gawk -f binarizer.awk datasetfile > matrixfile`

where datasetfile is the dataset from the UCI Machine Learning Repository, and matrixfile is the file into which the matrix is to be stored.

The script is written in Awk, a language designed for easy processing of text. What my script does is that as it makes a scan through the file, it records for each column, the different attribute values encountered (in order). This is stored in an associative array.

After the entire file has been scanned, it retrieves each line, and then based on the attribute in each column, replaces it with either a 0 or a 1. This is determined by checking whether the value lies in the first 1/3rd of all different values encountered.

The underlying meaning of the data is not entirely lost, as correlated attribute values will appear in the same order, and hence there is an increased probability that two or more correlated attributes will be assigned the same values.

The main Apriori program is in Java, and requires the following parameters:
1. matrixfile
2. columnsfile
3. minsupportpercentage
4. minconfpercentage

It would be advisable to run with a larger allocated memory by using -Xmx8g

