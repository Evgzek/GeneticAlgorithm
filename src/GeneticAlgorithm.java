import java.util.Random;

public class GeneticAlgorithm {

    private static final double LOWER_BOUND = -10; // нижняя граница для x и y
    private static final double UPPER_BOUND = 10; // верхняя граница для x и y
    private static final int POPULATION_SIZE = 100; // размер популяции
    private static final double MUTATION_RATE = 0.01; // коэффициент мутации
    private static final int MAX_GENERATIONS = 1000; // максимальное количество поколений

    // класс, представляющий геном (индивидуума) популяции
    private static class Genome {
        double x;
        double y;
        double fitness;

        Genome(double x, double y) {
            this.x = x;
            this.y = y;
            this.fitness = evaluateFitness(x, y);
        }

        // оценка приспособленности генома
        double evaluateFitness(double x, double y) {
            return 1 / (1 + x * x + y * y);
        }

        // скрещивание двух геномов
        Genome crossover(Genome other) {
            double childX = (this.x + other.x) / 2;
            double childY = (this.y + other.y) / 2;
            return new Genome(childX, childY);
        }

        // мутация генома
        void mutate() {
            Random random = new Random();
            if (random.nextDouble() < MUTATION_RATE) {
                double delta = (UPPER_BOUND - LOWER_BOUND) / 10;
                this.x += delta * random.nextGaussian();
                this.y += delta * random.nextGaussian();
                this.x = Math.max(Math.min(this.x, UPPER_BOUND), LOWER_BOUND);
                this.y = Math.max(Math.min(this.y, UPPER_BOUND), LOWER_BOUND);
                this.fitness = evaluateFitness(this.x, this.y);
            }
        }
    }

    // генерация начальной популяции
    private static Genome[] generateInitialPopulation() {
        Genome[] population = new Genome[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            double x = LOWER_BOUND + (UPPER_BOUND - LOWER_BOUND) * Math.random();
            double y = LOWER_BOUND + (UPPER_BOUND - LOWER_BOUND) * Math.random();
            population[i] = new Genome(x, y);
        }
        return population;
    }

    // селекция двух лучших геномов из популяции
    private static Genome[] selectParents(Genome[] population) {
        Genome[] parents = new Genome[2];
        double bestFitness = -1;
        for (Genome genome : population) {
            if (genome.fitness > bestFitness) {
                parents[1] = parents[0];
                parents[0] = genome;
                bestFitness = genome.fitness;
            } else if (genome.fitness > parents[1].fitness) {
                parents[1] = genome;
            }
        }
        return parents;
    }

    // основной цикл генетического алгоритма
    public static void main(String[] args) {
        Genome[] population = generateInitialPopulation();
        for (int generation = 1; generation <= MAX_GENERATIONS; generation++) {
// селекция двух лучших геномов
            Genome[] parents = selectParents(population);
            // создание новой популяции путем скрещивания двух родительских геномов
            Genome[] newPopulation = new Genome[POPULATION_SIZE];
            newPopulation[0] = parents[0];
            newPopulation[1] = parents[1];
            for (int i = 2; i < POPULATION_SIZE; i++) {
                Genome child = parents[0].crossover(parents[1]);
                child.mutate();
                newPopulation[i] = child;
            }

            // замена старой популяции новой
            population = newPopulation;

            // вывод лучшего результата на текущем поколении
            double bestFitness = -1;
            Genome bestGenome = null;
            for (Genome genome : population) {
                if (genome.fitness > bestFitness) {
                    bestFitness = genome.fitness;
                    bestGenome = genome;
                }
            }
            System.out.printf("Generation %d: x = %.3f, y = %.3f, fitness = %.3f\n",
                    generation, bestGenome.x, bestGenome.y, bestGenome.fitness);
        }
    }
}


