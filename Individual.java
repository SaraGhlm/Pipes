package project4;

/**
 *
 * @author Sara
 */
public class Individual {

    private final Pipe[][] map;
    private String chromosome;
    private double probability;

    public Individual(String choromosome, int n) {
        map = new Pipe[n][n];
        this.chromosome = choromosome;
    }

    public Individual(Pipe[][] map) {
        this.map = map;
        chromosome = "";
        makeChromosome();
    }

    private void makeChromosome() {
        for (int i = map.length - 1; i >= 0; i--) {
            for (int j = 0; j < map.length; j++) {
                chromosome += map[i][j].getPipeDirection();
            }
        }
    }

    public void makeMap(Individual parent) {
        int index = 0;
        for (int i = map.length - 1; i >= 0; i--) {
            for (int j = 0; j < map.length; j++) {
                Pipe pipe = new Pipe(parent.getMap()[i][j].getLocationType(),
                        parent.getMap()[i][j].getCost(), parent.getMap()[i][j].getType(),
                        Integer.parseInt(String.valueOf(chromosome.charAt(index++))));
                map[i][j] = pipe;
            }
        }
    }

    public Pipe[][] getMap() {
        return map;
    }

    public String getChromosome() {
        return chromosome;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
