package ru.pegas_agro.implement;

import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.Sentence;
import net.sf.marineapi.nmea.sentence.SentenceValidator;
import net.sf.marineapi.nmea.sentence.ZDASentence;
import ru.pegas_agro.service.ReadLogFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ReadLogFileImpl implements ReadLogFile {

    private final List<Sentence> sentences = new ArrayList<>();

    private final HashMap<Date, Integer> indexForDate = new HashMap<>();

    public HashMap<Date, List<Sentence>> splitByDays() {
        if (sentences.isEmpty()) {
            System.out.println("Считывание log файла...");
            readLogFile();
        }
        HashMap<Date, List<Sentence>> sentencesByDays = new HashMap<>();
        Date dateBefore = null;
        for (Date date : indexForDate.keySet()) {
            List<Sentence> filterSentences;
            if (sentencesByDays.isEmpty()) {
                filterSentences = sentences.subList(0, indexForDate.get(date)).stream()
                        .filter(sentence -> !sentence.getSentenceId().equals("ZDA"))
                        .collect(Collectors.toList());
            } else {
                filterSentences = sentences.subList(indexForDate.get(dateBefore), indexForDate.get(date))
                        .stream()
                        .filter(sentence -> !sentence.getSentenceId().equals("ZDA"))
                        .collect(Collectors.toList());
            }
            sentencesByDays.put(date, filterSentences);
            dateBefore = date;
        }
        return sentencesByDays;
    }

    private void readLogFile() {
        try {
            BufferedReader bf = new BufferedReader(new FileReader("logs/nmea.log"));
            while (bf.ready()) {
                String event = bf.readLine();
                if (SentenceValidator.isValid(event) && !event.contains(",,,")) {
                    SentenceFactory sf = SentenceFactory.getInstance();
                    Sentence sentence = sf.createParser(event);
                    if (sentence.getSentenceId().equals("ZDA")) {
                        ZDASentence zda = (ZDASentence) sentence;
                        indexForDate.put(zda.getDate().toDate(), sentences.size());
                    }
                    sentences.add(sentence);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
