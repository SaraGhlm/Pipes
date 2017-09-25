package project4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Sara
 */
public class Project4 {

    public static void main(String[] args) {
        String FILENAME = "input.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String current = br.readLine();
            Pipe[][] matrix;
            String[] parts = current.split(" ");
            int n = Integer.parseInt(parts[2]);
            matrix = new Pipe[n][n];

            int i = 0, j = 0;

            while ((current = br.readLine()) != null) {
                String[] pipes = current.split(" ");
                for (String pipe_data : pipes) {
                    pipe_data = pipe_data.substring(1, pipe_data.length() - 1);
                    parts = pipe_data.split(",");
                    Pipe pipe = new Pipe();
                    switch (parts[0]) {
                        case "A":
                            pipe.setLocationType(1);
                            pipe.setCost(0);
                            pipe.setPipeDirection(6);
                            pipe.setType(null);
                            break;
                        case "B":
                            pipe.setLocationType(2);
                            pipe.setCost(0);
                            pipe.setPipeDirection(6);
                            pipe.setType(null);
                            break;
                        case "2":
                            pipe.setLocationType(3);
                            pipe.setType(PipeType.straight);
                            pipe.setCost(Integer.parseInt(parts[2]));
                            if (parts[1].equals("0")) {
                                pipe.setPipeDirection(0);
                            } else {
                                pipe.setPipeDirection(1);
                            }
                            break;
                        default:
                            pipe.setLocationType(3);
                            pipe.setType(PipeType.knee);
                            pipe.setCost(Integer.parseInt(parts[2]));
                            switch (parts[1]) {
                                case "0":
                                    pipe.setPipeDirection(2);
                                    break;
                                case "1":
                                    pipe.setPipeDirection(3);
                                    break;
                                case "2":
                                    pipe.setPipeDirection(4);
                                    break;
                                default:
                                    pipe.setPipeDirection(5);
                                    break;
                            }
                            break;
                    }
                    matrix[i][j] = pipe;
                    j++;
                }
                i++;
                j = 0;
            }

            Individual individual = new Individual(matrix);
            //System.out.println("main DNA: " + individual.getChromosome());
            GA ga = new GA(individual);
            individual = ga.GeneticAlgorithm();

            System.out.println("Solution: ");
            for (int k = 0; k < individual.getMap().length; k++) {
                for (int l = 0; l < individual.getMap().length; l++) {
                    if (k == individual.getMap().length - 1 && l == 0) {
                        System.out.print("(A, -, -)\t");
                    } else if (l == individual.getMap().length - 1 && k == 0) {
                        System.out.print("(B, -, -)\t");
                    } else {
                        int cost = individual.getMap()[k][l].getCost();
                        if (individual.getMap()[k][l].getType() == PipeType.straight) {
                            if (individual.getMap()[k][l].getPipeDirection() == 0) {
                                System.out.print("(2, 0, " + cost + ")\t");
                            } else {
                                System.out.print("(2, 1, " + cost + ")\t");
                            }
                        } else {
                            switch (individual.getMap()[k][l].getPipeDirection()) {
                                case 2:
                                    System.out.print("(3, 0, " + cost + ")\t");
                                    break;
                                case 3:
                                    System.out.print("(3, 1, " + cost + ")\t");
                                    break;
                                case 4:
                                    System.out.print("(3, 2, " + cost + ")\t");
                                    break;
                                default:
                                    System.out.print("(3, 3, " + cost + ")\t");
                                    break;
                            }
                        }
                    }
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
