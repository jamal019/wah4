import javax.sound.midi.*;

public class TonleiterIllusion {
    Synthesizer synth;
    MidiChannel[] channels;

    public TonleiterIllusion() throws MidiUnavailableException {
        init();
    }

    private void init() throws MidiUnavailableException {
        synth = MidiSystem.getSynthesizer();
        channels = synth.getChannels();
        synth.open();
    }

    private void generateTon(int channel, int volume, int note, int duration) {
        channels[channel].programChange(Sequenzen.TRUMPET);
        channels[channel].noteOn(note, volume);

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        channels[channel].noteOff(note);
    }

    private void playIllusion() {
        int time = 200; // Dauer jedes Tons in Millisekunden

        // Aufsteigende Tonleiter
        for (int i = Sequenzen.C; i <= Sequenzen.Cokt; i++) {
            generateTon(0, 500, i, time);
        }

        // Absteigende Tonleiter
        for (int i = Sequenzen.Cokt; i >= Sequenzen.C; i--) {
            generateTon(1, 500, i, time);
        }

        // Schließen des Synthesizers
        if (synth != null) {
            synth.close();
        }
    }

    public static void main(String[] args) {
        try {
            TonleiterIllusion illusion = new TonleiterIllusion();
            illusion.playIllusion();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
}

class Sequenzen {
    public final static int C = 60, D = 62, E = 64, F = 65, G = 67, A = 69, H = 71, Cokt = 72;
    public final static int TRUMPET = 56; // MIDI-Programmnummer für Trompete
}
