package ru.pegas_agro.implement;

import net.sf.marineapi.nmea.sentence.GGASentence;
import net.sf.marineapi.nmea.sentence.Sentence;
import net.sf.marineapi.nmea.sentence.VTGSentence;
import net.sf.marineapi.nmea.util.Position;
import ru.pegas_agro.service.CalculateWay;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CalculateWayImpl implements CalculateWay {

    private final ReadLogFileImpl readLogFile = new ReadLogFileImpl();

    @Override
    public String calculateWay() {
        HashMap<Date, List<Sentence>> sentencesForDays = readLogFile.splitByDays();
        System.out.println("Вычисляю пройденный путь...");
        HashMap<Date, Double> wayForDays = new HashMap<>();
        StringBuilder response = new StringBuilder();
        for (Date date : sentencesForDays.keySet()) {
            wayForDays.put(date, calculateWayForDay(sentencesForDays.get(date)));
            response.append(date.toString()).append(" пройдено - ").append(String.format("%.2f", wayForDays.get(date)))
                    .append(" метров").append("\n");
        }
        return response.toString();
    }

    private Double calculateWayForDay(List<Sentence> sentences) {
        Position positionBefore = null;
        double track = 0;
        List<GGASentence> ggaSentences = filterGGASentence(sentences);
        for (GGASentence gga : ggaSentences) {
            if (positionBefore != null) {
                track += positionBefore.distanceTo(gga.getPosition());
            }
            positionBefore = gga.getPosition();
        }
        return track;
    }

    private List<GGASentence> filterGGASentence(List<Sentence> sentences) {
        List<GGASentence> ggaSentences = new ArrayList<>();
        for (int i = 0; i < sentences.size(); i++) {
            if (sentences.get(i).getSentenceId().equals("GGA") && i + 1 != sentences.size()) {
                if (sentences.get(i + 1).getSentenceId().equals("VTG")) {
                    VTGSentence sentence = (VTGSentence) sentences.get(i + 1);
                    if (sentence.getSpeedKmh() >= 1) {
                        GGASentence ggaSentence = (GGASentence) sentences.get(i);
                        ggaSentences.add(ggaSentence);
                    }
                }
            }
        }
        return ggaSentences;
    }
}
