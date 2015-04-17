package paladiff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class Paladiff {
    
    public static void main(String[] args) {
        System.out.println("paladiff v0.0.0.0.1");
        if (args.length == 0) {
            System.out.println("A test directory is required.");
            
            return;
        }
        
        File directory = new File(args[0]);
        if (!directory.isDirectory()) {
            System.out.println("Invalid test directory.");
            
            return;
        }
        
        List<String> tests = Paladiff.findTests(directory);
        
        int success = 0;
        int failure = 0;
        
        for (String entry : tests) {
            File input = new File(entry.concat(".in"));
            File output = new File(entry.concat(".out"));
            File actual = new File("test.tmp");
            
            if (!output.exists()) {
                System.out.print("S");
            }
            
            PrintStream ps = null;
            
            try {
                ps = new PrintStream(actual);
                System.setIn(new FileInputStream(input));
                System.setOut(ps);
                
                phoebe.Main.main(new String[] {});
                
                System.setIn(new FileInputStream(FileDescriptor.in));
                System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
                
                // Jöjjön a teszt
                
                BufferedReader actualReader = new BufferedReader(new InputStreamReader(new FileInputStream(actual)));
                BufferedReader expectedReader = new BufferedReader(new InputStreamReader(new FileInputStream(output)));
                
                StringBuilder actualString = new StringBuilder();
                StringBuilder expectedString = new StringBuilder();
                
                String line;
                
                while ((line = actualReader.readLine()) != null) {
                    actualString.append(line);
                }
                
                while ((line = expectedReader.readLine()) != null) {
                    expectedString.append(line);
                }
                
                if (expectedString.equals(actualString)) {
                    success++;
                    System.out.print(".");
                } else {
                    failure++;
                    System.out.print("F");
                }
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        }
        
    }
    
    private static List<String> findTests(File directory) {
        List<String> tests = new ArrayList<String>();
        File[] inputFiles = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".in");
            }
        }); 
        
        
        for (File file : inputFiles) {
            String testName = Paladiff.trimExtension(file);
            
            tests.add(testName);
        }
        
        return tests;
    }
    
    private static String trimExtension(File file) {
        String filename = file.getName();
        
        return filename.substring(0, filename.lastIndexOf('.'));        
    }

}
