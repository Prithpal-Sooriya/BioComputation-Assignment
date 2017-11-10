# Biocomputation Coursework

- We will be using Genetic Algorithms to classify some datasets.

- The type of Genetic Algorithms we will be using are Genetic Algorithms for Rule Set Production (GARP)

- For GARP, we will need to split the dataset (that we are working on) into TEST DATA and TRAINING DATA

- Fitness function = how close each individuals genes are to the training data's condition.

- Training = allow each member to learn if their condition is correct, but their output is not correct.
  - can be done by manually switching the value
  - can be done by giving a higher mutation rate just to the output part only.

- NOTE: that Rule based GA (GARP) can also use inversion crossover
  - inversion crossover = pick 2 points, and reverse the sub-list from those 2 points in the genes

- NOTE: We may need to consider fitness function VS diversity of new population.
  - by having the most diverse and fittest, then we can increase search space/search more broadly.

- NOTE: consider this when looking at test3.txt
  - http://www.obitko.com/tutorials/genetic-algorithms/crossover-mutation.php

- Expected output --> individual with least generic rules first, and more generic rules near the end

------------------------------------------
TODO COMPLETE:
- inversion crossover = DONE
- generic mutation function = DONE
- output mutation function = DONE
- fitness function that allows generics = DONE
- try running test1. = DONE, Now I have to decrease the number of generations it takes to get to the global optimal.
- try adding a conflict resolution (so if 2 genes match the same rule --> pick a gene which is "better" with most rules)
  - add an array in Rules to hold indices of genes which conflict
  - something to indicate which genes (of an individual) are being used on training and testing data
- Conflict Resolution = first gene that matches, DONE! --> How larry suggested doing this.

TODO NOT DONE:

- Generics --> how much generics should I include --> how to create an individual with first rules not as generic, and last rules more generic.
  - Make sure a rule's condition is not all made up of genetics = DONE
  
- add in inversion crossover (where we swap places of rules)

- Add in sorting population by fitness
  - Add in the subset of best parents swap for subset of worst children.

- Add in weights
  - where 1st rule for individual has weight of 1, and nth rule for individual has weight of N(length of genes)

- Task 3 Data analysis
  - clean data/reduce data
  - dimensional reduction

- Task 3 Genotype
  - bounds array? (2.563<=x<=2.836) --> Larry said do this.
    - mutation = change bounds a small amount higher or lower
    - crossover = swap bounds

  - doubles array? (2.633)
    - mutation = change the values a small amount higher or lower
    - crossover = ?
------------------------------------------
RESEARCH:
- added research folder
  - contains the information I was using for research and notes that I have made about them (can be used for my report).
