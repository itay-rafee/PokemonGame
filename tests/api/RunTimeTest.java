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

        // connected_components function
        startTime = System.nanoTime();
        for (int i = 0; i < 5; i++) {
            ga.connected_component(arr[i]);
        }
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        time1 = (float)totalTime / big_n;
        System.out.println("Time for connected_component function: " + time1 / 5);

        return true;
    }

    @Test
    void graph_with_100_nodes_test() throws InterruptedException {
        String file_name = "data/graph_100";
        int min = 0;
        int[] arr = {99, 98, 96, 94, 93};
        check(file_name, min, arr);

    }

    @Test
    void graph_with_10_thousand_nodes_test() {
        String file_name = "data/graph_10_thousand";
        int min = 0;
        int[] arr = {9999, 9998, 9997, 9996, 9995};
        check(file_name, min, arr);

    }

    @Test
    void graph_with_million_nodes_test() {
        String file_name = "data/graph_million";
        int min = 0;
        int[] arr = {};
        check(file_name, min, arr);
    }


}