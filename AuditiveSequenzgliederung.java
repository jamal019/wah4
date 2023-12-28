import javax.sound.midi.*;

public class AuditiveSequenzgliederung {
    private Synthesizer synth;
    private Sequencer sequencer;
    private Sequence sequence;

    public AuditiveSequenzgliederung() throws MidiUnavailableException, InvalidMidiDataException {
        init();
    }

    private void init() throws MidiUnavailableException, InvalidMidiDataException {
        synth = MidiSystem.getSynthesizer();
        synth.open();
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequence = new Sequence(Sequence.PPQ, 4);
    }

    private void addNote(Track track, int channel, int instrument, int note, int velocity, long startTick, long duration) throws InvalidMidiDataException {
        synth.getChannels()[channel].programChange(instrument);
        ShortMessage on = new ShortMessage();
        on.setMessage(ShortMessage.NOTE_ON, channel, note, velocity);
        track.add(new MidiEvent(on, startTick));

        ShortMessage off = new ShortMessage();
        off.setMessage(ShortMessage.NOTE_OFF, channel, note, velocity);
        track.add(new MidiEvent(off, startTick + duration));
    }

    public void createAndPlaySequence() throws InvalidMidiDataException, InterruptedException {
        Track track = sequence.createTrack();
        int noteDuration = 10; // Dauer der Noten

        // Erweiterte Melodien für Klavier und Klavier 2
        int[] pianoMelody = {60, 62, 64, 65, 67, 69, 71, 72, 74, 76, 77, 79, 81, 83, 84, 83, 81, 79, 77, 76, 74, 72, 71, 69, 67, 65, 64, 62, 60};
        long tick = 0;
        for (int note : pianoMelody) {
            addNote(track, 0, 0, note, 100, tick, noteDuration);
            tick += noteDuration;
        }

        // Melodie von Klavier 2, versetzt zur Melodie von Klavier 1
        int[] pianoMelody2 = {48, 50, 52, 53, 55, 57, 59, 60, 62, 64, 65, 67, 69, 71, 72, 71, 69, 67, 65, 64, 62, 60, 59, 57, 55, 53, 52, 50, 48};
        tick = pianoMelody.length * noteDuration / 2; // Startet in der Mitte der Klaviermelodie
        for (int note : pianoMelody2) {
            addNote(track, 1, 0, note, 80, tick, noteDuration);
            tick += noteDuration;
        }

        sequencer.setSequence(sequence);
        sequencer.start();

        // Warten bis das Abspielen fertig ist
        while (sequencer.isRunning()) {
            Thread.sleep(1000);
        }

        sequencer.close();
        synth.close();
    }

    public static void main(String[] args) {
        try {
            AuditiveSequenzgliederung demo = new AuditiveSequenzgliederung();
            demo.createAndPlaySequence();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
