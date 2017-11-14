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

- NOTE: dataset1 is quite/very random -> so we will not expect an individual to complete unless they have 32 genes (so hardcode match all the rules!)
- NOTE: dataset2 is ordered, and after speaking to Larry we can get the number of genes/rules for an individual down to 10 (or even lower to 5).

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

- Generics --> how much generics should I include --> how to create an individual with first rules not as generic, and last rules more generic.
  - Make sure a rule's condition is not all made up of genetics = DONE

- Add in sorting population by fitness = DONE
  - Add in the subset of best parents swap for subset of worst children.

- FIX FITNESS FUNCTION AND STOP CONDITION, AND CHECK SUBARRAY SWAP = DONE
  - After gaining some feedback, lets try a different approach to the fitness function
    - lets increase the fitness on if the condition AND output is correct (not for each bit that is correct)
      - as we want to reward an individual more when the whole rule is correct, not by if they are only partially correct...

- Task 3 Genotype = DONE (research)
  - bounds array? (2.563<=x<=2.836) --> Larry said do this.
    - mutation = change bounds a small amount higher or lower
    - crossover = swap bounds
  - doubles array? (2.633)
    - mutation = change the values a small amount higher or lower
    - crossover = ?

- Task 3 Data analysis = DONE (research)
  - clean data/reduce data
  - dimensional reduction

------------------------------------------------

TODO NOT DONE:

- Now that dataset2 works, we need to output to file
  - AND find out what schema/rules are used to match all rules.

- add in inversion crossover (where we swap places of rules)

- Add in weights (this is extra stuff... may do this)
  - where 1st rule for individual has weight of 1, and nth rule for individual has weight of N(length of genes)


------------------------------------------
RESEARCH:
- added research folder
  - contains the information I was using for research and notes that I have made about them (can be used for my report).
