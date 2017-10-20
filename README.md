# Biocomputation Coursework

- We will be using Genetic Algorithms to classify some datasets.

- The type of Genetic Algorithms we will be using are Genetic Algorithms for Rule Set Production (GARP)

- For GARP, we will need to split the dataset (that we are working on) into TEST DATA and TRAINING DATA

- Fitness function = how close each individuals genes are to the training data's condition.

- Training = allow each member to learn if their condition is correct, but their output is not correct.
  - can be done by manually switching the value
  - can be done by giving a higher mutation rate just to the output part only.

- NOTE: that Rule based GA (GARP) can also use inversion crossover
  - inversion crossover = pick 2 points, and reverse the sublist from those 2 points in the genes

- NOTE: We may need to consider fitness function VS diversity of new population.
  - by having the most diverse and fittest, then we can increase search space/search more broadly.

- NOTE: consider this when looking at test3.txt
  - http://www.obitko.com/tutorials/genetic-algorithms/crossover-mutation.php
