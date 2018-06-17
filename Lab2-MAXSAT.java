import java.util.*;
import java.io.*;
import java.lang.*;


import static java.lang.Math.random;

import java.net.URL;

public class vxc762 {

    public int satisfiability_checker(String input_clause, String bit_string) {

        for (int i = 1; i < input_clause.split(" ").length - 1; i++) {
            int val = Integer.parseInt(input_clause.split(" ")[i]);
            if (val > 0 && bit_string.charAt(Math.abs(val) - 1) == '1') {
                return 1;
            } else if (val < 0 && bit_string.charAt(Math.abs(val) - 1) == '0') {
                return 1;
            }
        }
        return 0;
    }

    public ArrayList<String> read_file(String file) {
        BufferedReader br = null;
        FileReader fr = null;
        List<String> store_example_data = new ArrayList<>();
        String property = System.getProperty("user.dir");
        String FILENAME = file;

        try {
            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                if(sCurrentLine.startsWith("c")){
                    continue;
                }
                else{
                    store_example_data.add(sCurrentLine);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        return (ArrayList<String>) store_example_data;
    }


    public String tournament(List<String> pp, List<Integer> fit) {
        Random rnd = new Random();
        int k = 5;
        int max_value = 0;

        List<Integer> index = new ArrayList<>();

        for (int j = 0; j < k; j++) {
            index.add(rnd.nextInt(pp.size() - 0) + 0);
        }

//        int max_value = Math.max(fit.get(index.get(0)), fit.get(index.get(1)));
        for (int m = 0; m < index.size(); m++) {
            if (fit.get(index.get(m)) > max_value) {
                max_value = fit.get(index.get(m));
            }
        }


        for (int i = 0; i < fit.size(); i++) {
            if (fit.get(i) == max_value) {
                return pp.get(i);
            }
        }
        return null;
    }

    public String mutation(String bitx) {
        int num = 0;
        String y = "";
        double chi = 0.3;
        Random rand = new Random();
        for (int i = 0; i < bitx.length(); i++) {
            if (num == 0) {
                num = 1;
                double ran = rand.nextDouble();
                if (ran < chi / bitx.length()) {
                    if (bitx.charAt(i) == '0') {
                        y = y + 1;
                    } else {
                        y = y + 0;
                    }
                } else {
                    y = y + bitx.charAt(i);
                }
            } else {
                double ran = rand.nextDouble();
                if (ran < chi / bitx.length()) {
                    if (bitx.charAt(i) == '0') {
                        y = y + 1;
                    } else {
                        y = y + 0;
                    }
                } else {
                    y = y + bitx.charAt(i);
                }
            }
        }
        return y;
    }

    public String crossover(String bitx, String bity) {
        int num = 0;
        String z = "";
        Random rand = new Random();
        for (int i = 0; i < bitx.length(); i++) {
            if (num == 0) {
                num = 1;
                if (bitx.charAt(i) == bity.charAt(i)) {
                    z = z + bitx.charAt(i);
                } else {
                    double ran = rand.nextDouble();
                    if (ran < 0.5) {
                        z = z + bitx.charAt(i);
                    } else {
                        z = z + bity.charAt(i);
                    }
                }
            } else {
                if (bitx.charAt(i) == bity.charAt(i)) {
                    z = z + bitx.charAt(i);
                } else {
                    double ran = rand.nextDouble();
                    if (ran < 0.5) {
                        z = z + bitx.charAt(i);
                    } else {
                        z = z + bity.charAt(i);
                    }
                }
            }
        }
        return z;
    }

    public static void main(String[] args) {

        vxc762 gl = new vxc762();
        String input_clause = "";
        List<Integer> return_value = new ArrayList<>();
        List<String> store_example_data = new ArrayList<>();
        String bit_string = "";

        int time_budget = 0;
        int repetitions = 0;
        int num_var = 0;


        //GET COMMAND LINE ARGUMENTS
        for (int i = 0; i < args.length; i++) {

            if ("-clause".equals(args[i])) {
                input_clause = args[i + 1];
                // SATISFIABILITY CHECKER
            } else if ("-assignment".equals(String.valueOf(args[i]))) {
                bit_string = args[i + 1];
            } else if ("-wdimacs".equals(String.valueOf(args[i]))) {

                //READ EXAMPLE FILE
                store_example_data = gl.read_file(args[i+1]);
                
                num_var = Integer.parseInt(store_example_data.get(0).split(" ")[2]);
            } else if ("-time_budget".equals(String.valueOf(args[i]))) {
                time_budget = Integer.parseInt(args[i + 1]);
            } else if ("-repetitions".equals(String.valueOf(args[i]))) {
                repetitions = Integer.parseInt(args[i + 1]);
            }
        }
        if (input_clause.length() > 1 && bit_string.length() > 0) {
            System.out.println(gl.satisfiability_checker(input_clause, bit_string));
        } else if (input_clause.length() < 1 && bit_string.length() > 0) {

            int clause = Integer.parseInt(store_example_data.get(0).split(" ")[3]);
            for (int k = 1; k <= clause; k++) {

                input_clause = store_example_data.get(k);
                return_value.add(gl.satisfiability_checker(input_clause, bit_string));

            }
            int result = 0;
            for (int l = 0; l < return_value.size(); l++) {
                result = result + return_value.get(l);
            }
            System.out.println(result);
        } else {
            for (int rep = 0; rep < repetitions; rep++) {
                int statis_clause = 0;
                String xbest = "";
                int generation = 0;
                String p_bit_string = "";
                int t = 0;
                List<String> in_clause = new ArrayList<>();
                int init_pop_size = 20;
                int fbest = 0;

                List<String> population = new ArrayList<>();
                List<Integer> fitness = new ArrayList<>();

                int clause = Integer.parseInt(store_example_data.get(0).split(" ")[3]);

                for (int k = 1; k <= clause; k++) {
                    in_clause.add(store_example_data.get(k));
                }
                
                for (int i = 0; i < init_pop_size; i++) {
                    int fit = 0;
                    p_bit_string = "";
                    for (int j = 0; j < num_var; j++) {
                        p_bit_string = p_bit_string + Math.round(Math.random());
                    }
                    population.add(p_bit_string);

                    int total = 0;
                    for (int k = 0; k < in_clause.size(); k++) {
                        if (gl.satisfiability_checker(in_clause.get(k), p_bit_string) == 1) {
                            total = total + 1;
                        }
                    }
                    fitness.add(total);
                }
                

                long starting_time = System.currentTimeMillis();
                while(true){

                    generation = generation + 1;

                    int max = Integer.MIN_VALUE;
                    int maxPos = -1;
                    
                    
                    for (int k = 0; k < fitness.size(); k++) {
                        int value = fitness.get(k);
                        if (value > max) {
                            max = value;
                            maxPos = k;
                        }
                        
                    }

                    fbest = fitness.get(maxPos);
                    xbest = population.get(maxPos);
                    t = generation * init_pop_size;
//                    
                    long current_time = System.currentTimeMillis();
                    long run_time = current_time - starting_time;
                    
                    if(run_time / 1000> time_budget ){break;}
                    
                    List<String> newpop = new ArrayList<>();
                    List<Integer> newfitness = new ArrayList<>();

                    for(int m = 0; m < init_pop_size; m++){
                        String xparent = gl.tournament(population,fitness);
                        String yparent = gl.tournament(population,fitness);

                        String xnew = gl.crossover(gl.mutation(xparent),gl.mutation(yparent));
                        newpop.add(xnew);
                        int total = 0;
                        for (int k = 0; k < in_clause.size(); k++) {
                            if (gl.satisfiability_checker(in_clause.get(k), p_bit_string) == 1) {
                                total = total + 1;
                            }
                        }
                        newfitness.add(total);
                    }
//
                    population = newpop;
                    fitness = newfitness;
//                   
                }
                System.out.println(t +"\t"+ fbest + "\t" + xbest);
                
            }
        }

    }

}


