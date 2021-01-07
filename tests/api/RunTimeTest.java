package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RunTimeTest {

    public static boolean check(String file_name, int min, int[] arr){
        System.out.println(" * My graph java *");
        DWGraph_Algo ga = new DWGraph_Algo();
        ga.load(file_name);
        long big_n = 1000000000;

        // connected_components function
        long startTime = System.nanoTime();
        ga.connected_components();
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Time for connected_components function: " + (double)totalTime / big_n);

        // shortestPath() function
        startTime = System.nanoTime();
        for (int i = 0; i < 5; i++) {
            ga.shortestPath(min, arr[i]);
        }
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        float time1 = (float)totalTime / big_n;
        System.out.println("Time for shortest_path function: " + time1 / 5);

        // connected_component function
        startTime = System.nanoTime();
        for (int i = 0; i < 5; i++) {
            ga.connected_component(arr[i]);
        }
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        time1 = (float)totalTime / big_n;
        System.out.println("Time for connected_component function: " + time1 / 5);
        System.out.println("\n\n");
        return true;
    }

    @Test
    void graph_with_10_nodes_test() throws InterruptedException {
        String file_name = "data/G_10_80_0.json";
        int min = 0;
        int[] arr = {9, 8, 7, 6, 5};
        System.out.println(" === 10_nodes ===");
        check(file_name, min, arr);

    }

    @Test
    void graph_with_100_nodes_test() {
        String file_name = "data/G_100_800_0.json";
        int min = 0;
        int[] arr = {99, 98, 97, 96, 95};
        System.out.println(" === 100_nodes ===");
        check(file_name, min, arr);

    }

    @Test
    void graph_with_1000_nodes_test() {
        String file_name = "data/G_1000_8000_0.json";
        int min = 0;
        int[] arr = {999, 998, 997, 996, 995};
        System.out.println(" === 1000_nodes ===");
        check(file_name, min, arr);
    }

    @Test
    void graph_with_10_thousand_nodes_test() {
        String file_name = "data/G_10000_80000_0.json";
        int min = 0;
        int[] arr = {9999, 9998, 9997, 9996, 9995};
        System.out.println(" === 10_thousand_nodes ===");
        check(file_name, min, arr);

    }

    @Test
    void graph_with_20_thousand_nodes_test() {
        String file_name = "data/G_20000_160000_0.json";
        int min = 0;
        int[] arr = {19999, 19998, 19997, 19996, 19995};
        System.out.println(" === 20_thousand_nodes ===");
        check(file_name, min, arr);

    }

    @Test
    void graph_with_30_thousand_nodes_test() {
        String file_name = "data/G_30000_240000_0.json";
        int min = 0;
        int[] arr = {29999, 29998, 29997, 29996, 29995};
        System.out.println(" === 30_thousand_nodes ===");
        check(file_name, min, arr);
    }


}