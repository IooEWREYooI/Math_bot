package telegrambots;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static telegrambots.service.Math_Prof_Formuls_bot.path;

public class PhotoDirectory {
    public static void convertSvg2Png(File svg, File png) {

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(svg);
            out = new FileOutputStream(png);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        out = new BufferedOutputStream(out);

        Transcoder transcoder = new PNGTranscoder();
        try {
            TranscoderInput input = new TranscoderInput(in);
            try {
                TranscoderOutput output = new TranscoderOutput(out);
                transcoder.transcode(input, output);
            } catch (TranscoderException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    out.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                in.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public static void convertURL2Svg(String urlStr, File file){
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            FileUtils.copyURLToFile(url, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void deleteSvgAndPng(Long chatId){
        try {
            Files.deleteIfExists(Path.of(path + chatId + ".svg"));
            Files.deleteIfExists(Path.of(path + chatId + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<String[]> getMapPhotos(){
        ArrayList<String[]> list  = new ArrayList<>();
        Iterator<File> fileIterator = getFileIteratorFromDirectory();
        while (fileIterator.hasNext()){
            File file = fileIterator.next();
            String[] arrayAnswerAndQuestion = file.getName().split(" _ ");
            String question = arrayAnswerAndQuestion[0];
            String answer = arrayAnswerAndQuestion[1].substring(0, arrayAnswerAndQuestion[1].length() - 4).replaceAll("`","/").replaceAll("--","->").replaceAll(" ","");
            list.add(new String[]{question, answer, file.getName()});
        }
        return list;
    }
    private static Iterator<File> getFileIteratorFromDirectory(){
        File directory = new File(path);
        File[] arrayFiles = directory.listFiles();
        ArrayList<File> fileList = new ArrayList<>();
        assert arrayFiles != null;
        Collections.addAll(fileList, arrayFiles);
        return fileList.iterator();
    }
}
