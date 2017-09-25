package project4;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Sara
 */
public class GA {

    private final Individual individual;
    private ArrayList<Individual> population;
    private int best_index, other_parent = -1;
    private boolean first_parent_chosen = false;

    public GA(Individual individual) {
        this.individual = individual;
        population = new ArrayList<>();
    }

    public Individual GeneticAlgorithm() {
        makePopulation(individual);
        Random rand = new Random();

        int count = 0;
        while (!oneFit() && count < 10000) {
            count++;
            ArrayList<Individual> new_population = new ArrayList<>();
            for (int i = 0; i < population.size(); i++) {
                Individual x = randomSelect();
                Individual y = randomSelect();
                Individual child = produce(x, y);

                int mutation = rand.nextInt(5);
                if (mutation == 0) {
                    child = mutate(child);
                }
                new_population.add(child);
            }
            population = new_population;
        }

        System.out.println("count: " + count);
        System.out.println("answer fitness " + fitness(population.get(best_index)));
        return population.get(best_index);
    }

    private boolean oneFit() {
        for (int i = 0; i < population.size(); i++) {
            double fitness = fitness(population.get(i));
            if (fitness >= 1000) {
                best_index = i;
                return true;
            }
        }
        return false;
    }

    private double fitness(Individual land) {
        boolean path_one_available = false;
        boolean path_two_available = false;
        double path_one_cost = distance(land.getMap().length - 1, 0, land.getMap().length - 1);
        double path_two_cost = distance(land.getMap().length - 1, 0, land.getMap().length - 1);

        //check path one
        if (land.getMap()[land.getMap().length - 1][1].getPipeDirection() == 0
                || land.getMap()[land.getMap().length - 1][1].getPipeDirection() == 3) {
            path_one_available = true;
            land.getMap()[land.getMap().length - 1][1].enteranceDirection("left");
            int[] answer = loop(land, land.getMap().length - 1, 1, land.getMap()[land.getMap().length - 1][1].getCost());
            int i = answer[1];
            int j = answer[2];
            if (i == land.getMap().length - 1 && j == 0) {
                path_one_cost = distance(i, j, land.getMap().length);
            } else if (i == 0 && j == land.getMap().length - 1) {
                path_one_cost = answer[0] + 1000;
            } else {
                path_one_cost = answer[0] + distance(i, j, land.getMap().length);
            }
        }

        //check path two
        if (land.getMap()[land.getMap().length - 2][0].getPipeDirection() == 1
                || land.getMap()[land.getMap().length - 2][0].getPipeDirection() == 2) {
            path_two_available = true;
            land.getMap()[land.getMap().length - 2][0].enteranceDirection("down");
            int[] answer = loop(land, land.getMap().length - 2, 0, land.getMap()[land.getMap().length - 2][0].getCost());
            int i = answer[1];
            int j = answer[2];
            if (i == land.getMap().length - 1 && j == 0) {
                path_two_cost = distance(i, j, land.getMap().length);
            } else if (i == 0 && j == land.getMap().length - 1) {
                path_two_cost = answer[0] + 1000;
            } else {
                path_two_cost = answer[0] + distance(i, j, land.getMap().length);
            }
        }

        if (path_two_available && path_one_available) {
            if (path_one_cost < path_two_cost) {
                return path_one_cost;
            } else {
                return path_two_cost;
            }
        } else if (path_one_available) {
            return path_one_cost;
        } else if (path_two_available) {
            return path_two_cost;
        } else {
            return distance(land.getMap().length - 1, 0, land.getMap().length);
        }
    }

    private double distance(int i, int j, int n) {
        double d = Math.sqrt(Math.pow(n - i, 2) + Math.pow(n - j, 2));
        return d;
    }

    private int[] loop(Individual land, int i, int j, int total_cost) {
        int size = land.getMap().length;
        boolean finish = false;
        while (!finish) {
            switch (land.getMap()[i][j].getLocationType()) {
                case 1:
                    // pipes made a loop which lead us to A again
                    finish = true;
                    break;
                case 2:
                    // Get to B! It is a valid path :D
                    finish = true;
                    break;
                default:
                    String direction = land.getMap()[i][j].getOutDirection();
                    switch (direction) {
                        case "left":
                            if (j - 1 >= 0) {
                                if (land.getMap()[i][j - 1].getPipeDirection() == 0
                                        || land.getMap()[i][j - 1].getPipeDirection() == 2
                                        || land.getMap()[i][j - 1].getPipeDirection() == 4) {
                                    j--;
                                    land.getMap()[i][j].enteranceDirection("right");
                                    total_cost = total_cost + land.getMap()[i][j].getCost();
                                } else {
                                    //Can't go this way. Not a desirable pipe
                                    finish = true;
                                }
                            } else {
                                //Can't go this way. it is a wall
                                finish = true;
                            }
                            break;
                        case "right":
                            if (j + 1 < size) {
                                if (land.getMap()[i][j + 1].getLocationType() == 2) {
                                    //Got left to B
                                    i++;
                                } else if (land.getMap()[i][j + 1].getPipeDirection() == 0
                                        || land.getMap()[i][j + 1].getPipeDirection() == 3
                                        || land.getMap()[i][j + 1].getPipeDirection() == 5) {
                                    j++;
                                    land.getMap()[i][j].enteranceDirection("left");
                                    total_cost = total_cost + land.getMap()[i][j].getCost();
                                } else {
                                    //Can't go this way. Not a desirable pipe
                                    finish = true;
                                }
                            } else {
                                //Can't go this way. it is a wall
                                finish = true;
                            }
                            break;
                        case "up":
                            if (i - 1 >= 0) {
                                if (land.getMap()[i - 1][j].getLocationType() == 2) {
                                    //Got beneath B
                                    i--;
                                } else if (land.getMap()[i - 1][j].getPipeDirection() == 1
                                        || land.getMap()[i - 1][j].getPipeDirection() == 2
                                        || land.getMap()[i - 1][j].getPipeDirection() == 5) {
                                    i--;
                                    land.getMap()[i][j].enteranceDirection("down");
                                    total_cost = total_cost + land.getMap()[i][j].getCost();
                                } else {
                                    //Can't go this way. Not a desirable pipe
                                    finish = true;
                                }
                            } else {
                                //Can't go this way. it is a wall
                                finish = true;
                            }
                            break;
                        default:
                            if (i + 1 < size) {
                                if (land.getMap()[i + 1][j].getPipeDirection() == 1
                                        || land.getMap()[i + 1][j].getPipeDirection() == 3
                                        || land.getMap()[i + 1][j].getPipeDirection() == 4) {
                                    i++;
                                    land.getMap()[i][j].enteranceDirection("up");
                                    total_cost = total_cost + land.getMap()[i][j].getCost();
                                } else {
                                    //Can't go this way. Not a desirable pipe
                                    finish = true;
                                }
                            } else {
                                //Can't go this way. it is a wall
                                finish = true;
                            }
                            break;
                    }
                    break;
            }
        }

        return new int[]{total_cost, i, j};
    }

    private Individual mutate(Individual i) {
        int n = i.getChromosome().length();
        Random rand = new Random();
        int c = rand.nextInt(n - 2);
        c++;
        String cell = i.getChromosome().substring(c, c + 1);
        String new_cell;
        switch (cell) {
            case "0":
                new_cell = "1";
                break;
            case "1":
                new_cell = "0";
                break;
            case "2":
                int r = rand.nextInt(3);
                switch (r) {
                    case 0:
                        new_cell = "3";
                        break;
                    case 1:
                        new_cell = "4";
                        break;
                    default:
                        new_cell = "5";
                        break;
                }
                break;
            case "3":
                r = rand.nextInt(3);
                switch (r) {
                    case 0:
                        new_cell = "2";
                        break;
                    case 1:
                        new_cell = "4";
                        break;
                    default:
                        new_cell = "5";
                        break;
                }
                break;
            case "4":
                r = rand.nextInt(3);
                switch (r) {
                    case 0:
                        new_cell = "2";
                        break;
                    case 1:
                        new_cell = "3";
                        break;
                    default:
                        new_cell = "5";
                        break;
                }
                break;
            default:
                r = rand.nextInt(3);
                switch (r) {
                    case 0:
                        new_cell = "2";
                        break;
                    case 1:
                        new_cell = "3";
                        break;
                    default:
                        new_cell = "4";
                        break;
                }
                break;
        }

        String chromosome = i.getChromosome().substring(0, c) + new_cell + i.getChromosome().substring(c + 1);
        Individual i1 = new Individual(chromosome, i.getMap().length);
        i1.makeMap(i);
        return i1;
    }

    private Individual produce(Individual x, Individual y) {
        int n = x.getChromosome().length();
        Random rand = new Random();
        int c = rand.nextInt(n - 2);
        c++;
        String choromosome = x.getChromosome().substring(0, c) + y.getChromosome().substring(c);
        Individual child = new Individual(choromosome, x.getMap().length);
        child.makeMap(x);
        return child;
    }

    private Individual randomSelect() {
        int totalSum = 0;

        for (int i = 0; i < population.size(); i++) {
            if (i != other_parent) {
                population.get(i).setProbability(fitness(population.get(i)));
                totalSum += population.get(i).getProbability();
            }
        }
        Random rand = new Random();
        int index = rand.nextInt(totalSum);
        double sum = 0;
        int i = 0;
        while (sum < index) {
            if (first_parent_chosen) {
                if (i == other_parent) {
                    i++;
                }
            }
            sum = sum + population.get(i++).getProbability();
        }

        if (!first_parent_chosen) {
            first_parent_chosen = true;
            other_parent = i - 1;
        } else {
            first_parent_chosen = false;
            other_parent = -1;
        }

        int mIndex = Math.max(0, i - 1);
        return population.get(mIndex);
    }

    private void makePopulation(Individual land) {
        int initial_population_size = 100;
        Random rand = new Random();
        for (int i = 0; i < initial_population_size; i++) {
            Pipe[][] new_land = new Pipe[land.getMap().length][land.getMap().length];
            for (int j = 0; j < land.getMap().length; j++) {
                for (int k = 0; k < land.getMap().length; k++) {
                    if (land.getMap()[j][k].getLocationType() == 3) {
                        int change = rand.nextInt(2);
                        if (change == 1) {
                            Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                    land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), land.getMap()[j][k].getPipeDirection());
                            new_land[j][k] = pipe;
                        } else {
                            if (land.getMap()[j][k].getType() == PipeType.straight) {
                                if (land.getMap()[j][k].getPipeDirection() == 1) {
                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 0);
                                    new_land[j][k] = pipe;
                                } else {
                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 1);
                                    new_land[j][k] = pipe;
                                }
                            } else {
                                if (land.getMap()[j][k].getPipeDirection() != 6) {
                                    switch (land.getMap()[j][k].getPipeDirection()) {
                                        case 3: {
                                            int rotation = rand.nextInt(3);
                                            switch (rotation) {
                                                case 0: {
                                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 5);
                                                    new_land[j][k] = pipe;
                                                    break;
                                                }
                                                case 1: {
                                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 2);
                                                    new_land[j][k] = pipe;
                                                    break;
                                                }
                                                default: {
                                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 4);
                                                    new_land[j][k] = pipe;
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                        case 5: {
                                            int rotation = rand.nextInt(3);
                                            switch (rotation) {
                                                case 0: {
                                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 3);
                                                    new_land[j][k] = pipe;
                                                    break;
                                                }
                                                case 1: {
                                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 2);
                                                    new_land[j][k] = pipe;
                                                    break;
                                                }
                                                default: {
                                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 4);
                                                    new_land[j][k] = pipe;
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                        case 4: {
                                            int rotation = rand.nextInt(3);
                                            switch (rotation) {
                                                case 0: {
                                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 5);
                                                    new_land[j][k] = pipe;
                                                    break;
                                                }
                                                case 1: {
                                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 2);
                                                    new_land[j][k] = pipe;
                                                    break;
                                                }
                                                default: {
                                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 3);
                                                    new_land[j][k] = pipe;
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                        case 2: {
                                            int rotation = rand.nextInt(3);
                                            switch (rotation) {
                                                case 0: {
                                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 5);
                                                    new_land[j][k] = pipe;
                                                    break;
                                                }
                                                case 1: {
                                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 3);
                                                    new_land[j][k] = pipe;
                                                    break;
                                                }
                                                default: {
                                                    Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                                            land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(), 4);
                                                    new_land[j][k] = pipe;
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                    } else {
                        Pipe pipe = new Pipe(land.getMap()[j][k].getLocationType(),
                                land.getMap()[j][k].getCost(), land.getMap()[j][k].getType(),
                                land.getMap()[j][k].getPipeDirection());
                        new_land[j][k] = pipe;
                    }
                }
            }
            Individual new_individual = new Individual(new_land);
            population.add(new_individual);
        }
    }
}
