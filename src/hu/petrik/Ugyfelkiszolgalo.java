package hu.petrik;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.net.Socket;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Ugyfelkiszolgalo implements Runnable{
    private HashMap<String, Idojaras> elorejelzesek;
    Socket kapcsolat;

    public Ugyfelkiszolgalo(Socket kapcsolat) {
        elorejelzesek = new HashMap<>();
        beolvas();
        this.kapcsolat = kapcsolat;
    }

    @Override
    public void run() {
        try {
            DataInputStream klienstol = new DataInputStream(kapcsolat.getInputStream());
            DataOutputStream kliensnek = new DataOutputStream(kapcsolat.getOutputStream());

            int menu;

            do {
                menu = klienstol.readInt();
                switch (menu) {
                    default:
                        kliensnek.writeUTF("Nem található ilyen opció");
                        break;
                    case 0:
                        kliensnek.writeUTF("Kilépés...");
                        break;
                    case 1:
                        String s1= "";
                        for (Map.Entry<String, Idojaras> entry : elorejelzesek.entrySet()) {
                            s1 += entry.getKey() + entry.getValue() + System.lineSeparator();
                        }
                        kliensnek.writeUTF(s1);
                        break;
                    case 2:
                        String s2 = "Nincs ilyen megye";
                        String megye = klienstol.readUTF();
                        if (elorejelzesek.containsKey(megye))
                            s2 = elorejelzesek.get(megye).toString();
                        kliensnek.writeUTF(s2);
                        break;
                    case 3:
                        Integer legnagyobb = elorejelzesek.entrySet().stream()
                                .map(stringWeatherEntry -> stringWeatherEntry.getValue().getMaiElorejelzes().getMax()).max(Comparator.naturalOrder()).get();
                        kliensnek.writeUTF("Legmagasabb hőmérséklet: " + legnagyobb);
                        break;
                    case 4:
                        Integer legkisebb = elorejelzesek.entrySet().stream()
                                .map(stringWeatherEntry -> stringWeatherEntry.getValue().getMaiElorejelzes().getMax()).min(Comparator.naturalOrder()).get();
                        kliensnek.writeUTF("Legkisebb hőmérséklet: " + legkisebb);
                        break;
                    case 5:
                        long havasCount = elorejelzesek.entrySet().stream()
                                .map(stringWeatherEntry -> stringWeatherEntry.getValue().getMaiElorejelzes().equals("Snow")).count();
                        kliensnek.writeUTF(String.format("%d db helyen fog esni hó ma", havasCount));
                        break;

                }
                kliensnek.flush();
            }while (menu != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void beolvas() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("weather.txt"));
            br.readLine();
            String sor = br.readLine();
            while(sor != null) {
                Idojaras idojaras = new Idojaras(sor);
                String megye = idojaras.getMegye();
                elorejelzesek.put(megye, idojaras);
                sor = br.readLine();
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
