import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Termins {
    public static void main(String[] args) throws IOException {
        addAllWordInFile();
        createUniqueWordsFile();
        System.out.println(listCountWords);
        frequency();
        idf();
        write();
    }

    static int N = 169;
    static Pattern pattern = Pattern.compile("[А-Я][а-я]+|[а-я][а-я]+");
    static List<String> list = new ArrayList<>();
    static List<Integer> listCountWords = new ArrayList<>();
    static double count = 0;
    static Set<String> set = new HashSet<>();
    static Map<String, Double> map = new HashMap<>();
    static HashMap<String, Map<String, Double>> tfMap = new HashMap<>();
    static List<Double> idfMap = new ArrayList<>();

    public static void write() {
        String word;
        for (Map.Entry<String, Map<String, Double>> item : tfMap.entrySet()) {
            try {
                Map<String, Double> getVal = item.getValue();

                System.out.println("sizeFile1 = " + getVal.size());
                word = "wordTfIdf" + item.getKey();
                BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/Александра/IdeaProjects/tf_idf/src/tf_idf/" + word + ".txt"));
                for (Map.Entry<String, Double> item2 : getVal.entrySet()) {
                    writer.write(item2.getKey() + " ");
                    writer.write(item2.getValue() + " ");
                    writer.write(idfMap.get(0) + " ");
                    writer.write("\n");
                    idfMap.remove(0);
                }
                writer.close();
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

    }

    public static void frequency() {
        try {
            String name;
            String nameOut;
            double tf;
            for (int i = 0; i < N; i++) {
                if (listCountWords.get(i) != 0) {
                    Map<String, Double> mapTfNew = new HashMap<>();
                    name = "C:\\Users\\Александра\\IdeaProjects\\tf_idf\\src\\unique\\uniqueWords" + i + ".txt";
                    nameOut = "C:/Users/Александра/IdeaProjects/tf_idf/src/fileWords/word" + i + ".txt";
                    map = countUniqueWords(name, nameOut);
                    for (Map.Entry<String, Double> item : map.entrySet()) {
                        if (item.getValue() != 0) {
                            tf = item.getValue() / listCountWords.get(i);
                            mapTfNew.put(item.getKey(), tf);
                        }
                    }
                    tfMap.put(Integer.toString(i), mapTfNew);
                }
            }
            map.clear();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void idf() {
        try {
            String str;
            double count = 1;
            double idf;
            for (int i = 0; i < N; i++) {
                BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Александра\\IdeaProjects\\tf_idf\\src\\unique\\uniqueWords" + i + ".txt"));
                while (reader.ready()) {
                    str = reader.readLine();
                    if (!str.isEmpty()) {
                        count = countWordsInAllPages(str);
                        idf = N / count;
                        idfMap.add(idf);
                    }
                }
                try {
                    if (reader != null) reader.close();
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            System.out.println("sizeIdf = " + idfMap.size());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static double countWordsInAllPages(String givenWord) {
        double count = 0;
        try {
            String str;
            for (int i = 0; i < N; i++) {
                BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Александра\\IdeaProjects\\tf_idf\\src\\unique\\uniqueWords" + i + ".txt"));
                while (reader.ready()) {
                    str = reader.readLine();
                    if (!str.isEmpty()) {
                        if (str.equals(givenWord)) {
                            count++;
                            break;
                        }
                    }
                }
                try {
                    if (reader != null) reader.close();
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return count;
    }

    public static Map<String, Double> countUniqueWords(String input, String output) throws IOException {
        BufferedReader readerSetWords = new BufferedReader(new FileReader(input));
        String currentLine;
        Map<String, Double> words = new HashMap<>();
        while (readerSetWords.ready()) {
            currentLine = readerSetWords.readLine().toLowerCase();
            count = returnCountWord(currentLine, output);
            words.put(currentLine, count);
        }
        return words;
    }

    public static int returnCountWord(String input, String output) throws IOException {
        BufferedReader reader = null;
        String currentLine;
        Matcher matcher;
        int count = 0;
        Pattern pattern = Pattern.compile(input);
        reader = new BufferedReader(new FileReader(output));
        while (reader.ready()) {
            currentLine = reader.readLine().toLowerCase();
            matcher = pattern.matcher(currentLine);
            if (matcher.find()) {
                count++;
            }
        }
        try {
            if (reader != null) reader.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return count;
    }

    public static void addAllWordInFile() {
        try {
            String currentLine;
            Matcher matcher;
            BufferedReader reader;
            File file;
            String name;
            for (int i = 0; i < N; i++) {
                file = new File(("C:\\Users\\Александра\\IdeaProjects\\tf_idf\\src\\files\\file" + i + ".txt"));
                if (file.exists()) {
                    System.out.println(i);
                    reader = new BufferedReader(new FileReader(file));
                    while (reader.ready()) {
                        currentLine = reader.readLine();
                        matcher = pattern.matcher(currentLine);
                        while (matcher.find()) {
                            currentLine = matcher.group().toLowerCase();
                            list.add(currentLine);
                        }
                    }
                    reader.close();
                }
                removeStopWords();
                name = "word" + i;
                BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/Александра/IdeaProjects/tf_idf/src/fileWords/" + name + ".txt"));
                for (String s : list) {
                    if (!s.equals("")) writer.write(s + "\n");
                }
                listCountWords.add(list.size());
                writer.close();
                list.clear();
            }

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void removeStopWords() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Александра\\IdeaProjects\\tf_idf\\src\\result\\stop_words_russian.txt"));
            String str;
            ArrayList<String> stopWords = new ArrayList<>();
            while (reader.ready()) {
                str = reader.readLine();
                if (!str.isEmpty()) {
                    stopWords.add(str);
                }
            }
            ArrayList<String> allWords = new ArrayList<>(list);
            allWords.removeAll(stopWords);
            list = new ArrayList<>(allWords);
        } catch (IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    public static void createUniqueWordsFile() {
        try {
            String currentLine;
            Matcher matcher;
            BufferedReader reader;
            File file;
            String unique;
            String word;
            for (int i = 0; i < N; i++) {
                file = new File(("C:\\Users\\Александра\\IdeaProjects\\tf_idf\\src\\fileWords\\word" + i + ".txt"));
                if (file.exists()) {
                    System.out.println(i);
                    reader = new BufferedReader(new FileReader(file));
                    while (reader.ready()) {
                        currentLine = reader.readLine();
                        matcher = pattern.matcher(currentLine);
                        while (matcher.find()) {
                            currentLine = matcher.group().toLowerCase();
                            set.add(currentLine);
                        }
                    }
                    reader.close();
                }
                removeStopWords();
                unique = "uniqueWords" + i;
                word = "wordTfIdf" + i;
                BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/Александра/IdeaProjects/tf_idf/src/unique/" + unique + ".txt"));
                for (String s : set) {
                    if (!s.equals("")) writer.write(s + "\n");
                }
                set.clear();
                writer.close();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
