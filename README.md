# Biocomputation Coursework

- DATA3.txt SCHEMA
[0.48632228<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=0.50485134, 0.0<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=0.50000423] = 0
[0.0<=x<=0.50507873, 0.3429683<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=0.50218636, 0.0<=x<=1.0, 0.5024465<=x<=1.0] = 0
[0.50785315<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=1.0, 0.49841252<=x<=1.0, 0.0<=x<=1.0] = 1
[0.48795205<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=0.53951913, 0.49359575<=x<=1.0] = 0
[0.0<=x<=1.0, 0.4997855<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=1.0] = 1
[0.28319043<=x<=0.3670704, 0.0<=x<=0.2731698, 0.37704974<=x<=0.78111595, 0.0<=x<=0.08684872, 0.0<=x<=1.0, 0.89201987<=x<=0.9022813] = 0
[0.0<=x<=0.99045765, 0.0<=x<=0.7065567, 0.0<=x<=1.0, 0.5042222<=x<=1.0, 0.0<=x<=1.0, 0.4876416<=x<=1.0] = 1
[0.49974912<=x<=1.0, 0.0<=x<=0.63624394, 0.41899624<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=0.574821, 0.0<=x<=0.56863105] = 1
[0.0<=x<=1.0, 0.0<=x<=0.8492273, 0.0<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=1.0, 0.0<=x<=1.0] = 0
[0.329029<=x<=1.0, 0.074089244<=x<=0.6225577, 0.49910226<=x<=1.0, 0.15073553<=x<=0.9906155, 0.24000885<=x<=0.75608385, 0.7790715<=x<=1.0] = 1


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

- [x] inversion crossover = DONE

- [x] generic mutation function = DONE

- [x] output mutation function = DONE

- [x] fitness function that allows generics = DONE

- [x] try running test1. = DONE, Now I have to decrease the number of generations it takes to get to the global optimal.

- [x] try adding a conflict resolution (so if 2 genes match the same rule --> pick a gene which is "better" with most rules)
  - add an array in Rules to hold indices of genes which conflict
  - something to indicate which genes (of an individual) are being used on training and testing data

- [x] Conflict Resolution = first gene that matches, DONE! --> How larry suggested doing this.

- [x] Generics --> how much generics should I include --> how to create an individual with first rules not as generic, and last rules more generic.
  - Make sure a rule's condition is not all made up of genetics = DONE

- [x] Add in sorting population by fitness = DONE
  - Add in the subset of best parents swap for subset of worst children.

- [x] FIX FITNESS FUNCTION AND STOP CONDITION, AND CHECK SUBARRAY SWAP = DONE
  - After gaining some feedback, lets try a different approach to the fitness function
    - lets increase the fitness on if the condition AND output is correct (not for each bit that is correct)
      - as we want to reward an individual more when the whole rule is correct, not by if they are only partially correct...

- [x] Task 3 Genotype = DONE (research)
  - bounds array? (2.563<=x<=2.836) --> Larry said do this.
    - mutation = change bounds a small amount higher or lower
    - crossover = swap bounds
  - doubles array? (2.633)
    - mutation = change the values a small amount higher or lower
    - crossover = ?

- [x] Task 3 Data analysis = DONE (research)
  - clean data/reduce data
  - dimensional reduction

- [x] Task3: blend crossover
- [x] Task3: when bounds become <0 || >1
  - [x] could limit bounds to be at 0 or 1 (however after running it with this, all bounds become stuck at 0 and 1)]
  - [x] could "bounce" the bounds back in
    - so `if(min-creep < 0) min+creep;` --> what if min = 0.5 and creep = 0.56 (then stuck..)
  - [x] could let bounds "phase" and revalidate bounds (out bottom/0, in top/1)
    - `min-creep; if(min<0) 1 + min;` //as min becomes -ve then just add to 1
  - [x] could to an "absolute" on 0 and 1
    - `min-creep; if(min<0) min = -min; if(min>1) min -=1;`

- NOTE: Larry said keep it simple
  - normal crossover
  - relatively high omega offset (for mutation)
  - ...
------------------------------------------------

TODO NOT DONE:

- Now that dataset2 works, we need to output to file
  - AND find out what schema/rules are used to match all rules.

- Add in weights (this is extra stuff... may do this)
  - where 1st rule for individual has weight of 1, and nth rule for individual has weight of N(length of genes)

- WEKA for extra algorithms
  - e.g. gradient descent, MLP/NN, J48, C4.5



------------------------------------------
RESEARCH:
- added research folder
  - contains the information I was using for research and notes that I have made about them (can be used for my report).

- Think about using a neural network for dataset 3 to compare

- research fuzzy classifiers
  - https://github.com/Apoptoz/FL_Classifier/blob/master/article.pdf


- http://www.cs.bham.ac.uk/~rjh/courses/NatureInspiredDesign/2010-11/StudentWork/Group3/The%20Use%20of%20Evolutionary%20Algorithms%20in%20Data%20Mining.pdf


- https://www.drmgrdu.ac.in/Thesis_doc/Documents/V.N.Rajavarman%20Thesis/final.pdf
  - - 2.3.8 Issues in EA
  - -3.3 The Strengths of GAs
  - 4.2.1 Individual’s encoding
