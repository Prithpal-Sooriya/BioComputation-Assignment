    # Dataset 3

## data analysis
- This dataset is massive (2000 rules containing 6 features/conditions to an output)

### Clean the data
- randomize the data so we can then get a better set of data
  - research this in more detail

### Feature Dimensional Reduction
- See if it is possible to do to this dataset without/little data loss and corruption
  - research some different methods to do this.

### Training and Testing data
- Split the data into a trainingSet and validationSet
  - trainingSet is for training the population
  - validationSet is for testing on the population

- Random pick data from trainingSet.

- If GA matches trainingSet completely we may face an issue with overfitting
  - will not work on all validation set.

## Structure of dataset 3
- This dataset has a set of features where the conditions are floating point and the output is binary
    - Example from file

    `0.981136 0.369132 0.498354 0.067417 0.422276 0.803662 0`

- Each floating point condition is bounded between 0-1

## Genotype
- Each individual will have a set/array or Rules
- Each individual
- Each individual will have a fitness

```java
public Individual{
  Rule[] genes;
  double fitness;

  public Individual(int geneSize) {
    genes = new Rule[geneSize];

    //initialize and populate genes.
  }

}
```

- I will need a way to randomize the genes
  - thus randomize the rules
    - thus randomize bounds
    - need to check if random bounds is valid (so lowerbound < upperbound)

### Composition of a Rule & Bound
- Each rule will have:
  - Array of Bound's [Number of Conditions]
  - An output

  ```java
  public Rule {
    Bound bounds[];
    int output;

    public Rule(int conditionSize){
      bounds = new Bound[conditionSize];

      for(int i = 0; i < conditionSize; i++){
        bounds[i] = new Bound();
      }
    }
  }

  ```
- Each Bound will have:
  - lower bound : Float
  - upper bound : Float

  ```java
  public Bound {
    float upperBound;
    float lowerBound;

    public Bound() {
      //randomize upper and lowerbound
    }

    public Bound(float upperBound, float lowerBound) {
      this.upperBound = upperBound;
      this.lowerBound = lowerBound;
    }
  }
  ```

## Fitness function
- Same as boolean/binary datasets

### Fitness on correct Rule
- loop through training set
  - for each rule the EXACTLY matches --> fitness++;
  - break out if conditions match (even if outputs do not match)
    - so only 1 gene is used to test rule.

### Fitness on correct bits
- loop through training set
  - for each condition that matches --> tempFitness++;
  - IF all conditions match AND output matches --> tempFitness*=2
    - this is so we can reward individuals who match all conditions and rules
  - IF outputs dont match, still break out (so no other rule is tested)

## Crossover
- Decode genes?
  - So just becomes a long array of floats[], maybe just decode rules?
    - if N conditions, then N*2 floats (for each bound)
  - Handle swapping outputs separately

```java
public void decodeGenes(){
  float[] allConditions = new float[Rule.getConditionLength*2*rules.length];
  int[] allOutputs = new float[rules.length];

  //decode conditions and outputs
  int outputIndex = 0;
  for(int i = 0; i < rules.length; i++){
    for(int j = 0; j < rules[i].getConditionLength; j++){
      int lowerBoundIndex = (i*rule[i].getConditionLength) + (j*2); //offset of each rule + offset for each bound
      int upperBoundIndex = (i*rule[i].getConditionLength) + (j*2) + 1;
      allConditions[lowerBoundIndex] = rule[i].getBounds(j).getLowerBound();
      allConditions[upperBoundIndex] = rule[i].getBounds(j).getUpperBound();
    }
    allOutputs[i] = rules[i].getOutput();
  }

}
```

- Encode genes
  - convert the float[] array back into rules
    - validate each bound (if lowerBound > upperBound then swap)

```java
public void encodeGenes(float[] allConditions, int[] allOutputs){

  Rule[] newGenes = new Rule[rules.length];

  for(int i = 0; i < rules.length; i++){
    for(int j = 0; j < rules[i].getConditionLength; j++) {
      int lowerBoundIndex = (i*rule[i].getConditionLength) + (j*2); //offset of each rule + offset for each bound
      int upperBoundIndex = (i*rule[i].getConditionLength) + (j*2) + 1;

      /* Validate bounds */
      //need to do this... maybe just if condition to swap if lowerBound>upperBound

      rules[i].getBounds(j).setLowerBound(allConditions[lowerBoundIndex]);
      rules[i].getBounds(j).setUpperBound(allConditions[upperBoundIndex]);
    }
    rules[i].setOutput(allOutputs[i]);
  }

}
```

- single crossover
  - after every (rule.getConditionLength) float, we will swap over output if necessary

- blending crossover
  - discreet crossover
  - pick 2 floats (1 from each parent) and find the mean of them
    - e.g. parent1float[6] = 0.8, parent2float[6] = 0.2 --> parent1float[6] = 0.5, parent2float[6] = 0.5;


## Mutation
- Creeping
  - change the bounds just slightly +/- Math.random().
  - Validate bounds after (to make sure lowerBound>upperBound)
    - worst case we can just swap upper and lower bounds.
