# Task 1
For this task, we have to classify the data from data1.txt.

## Problem domain
- Data 1's format is in the form `00111 0`
  - the left hand side is for the input.
  - the right hand side is for the output
  - So from this case `00111` should be classified as `1`

## Genotype

### Genotype = DNA format
- To solve this sort of problem we will be using GA Rule Production (GARP)
  - IF `00111` THEN `1`

- So lets create a `class Rule()` where it takes a condition (the `IF` part) and action (the `THEN` part).

- Later on we will be adding `#` to indicate that a bit could be a 1 or a 0 (like a wildcard)
  - from this we can then use the population to classify new data.

### Genotype = fitness function
- The fitness function will be a comparison between the training data and the population.
  - We will only be comparing the condition part --> as this is the part that needs to be optimised.
- Each correct comparison = increment the fitness of that individual.
- We could also consider how well


- In the future, other additions can be added to the fitness function (e.g. Quadratic/Exponential fitness, and biases to the fitness).
- We also need to balance diversity and most fittest individual (to prevent getting stuck in local optima).
