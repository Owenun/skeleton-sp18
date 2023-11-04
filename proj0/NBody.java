import edu.princeton.cs.algs4.Count;
import edu.princeton.cs.algs4.StdAudio;

import java.util.ArrayList;

public class NBody {

    public static double readRadius(String path) {

        In in = new In(path);
        in.readInt();
        return in.readDouble();

    }

    public static Planet[] readPlanets(String path, int count){

        Planet[] planets;
        planets = new Planet[count];
        In in = new In(path);

        in.readInt();
        in.readDouble();

        for (int i = 0; i < count; i++) {
            double xxPos = in.readDouble();
            double yyPox = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String img = in.readString();

            Planet p = new Planet(xxPos, yyPox, xxVel, yyVel, mass, img);
            planets[i] = p;

        }
        return planets;
    }

    public static void main(String[] args) {


        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];

        double radius = NBody.readRadius(filename);
        int count = new In(filename).readInt();

        Planet[] planets = NBody.readPlanets(filename, count);
        StdDraw.enableDoubleBuffering();
        String background = "./images/starfield.jpg";
        StdDraw.setScale(-radius, radius);
        StdDraw.picture(0, 0, background);
        StdDraw.show();
        for (Planet p: planets) {
            p.imgFileName = "images/" + p.imgFileName;
            p.draw();
        }
        StdDraw.show();



        double timeVar = 0;
        while (timeVar < T) {
            ArrayList<Double> xForces = new ArrayList<>();
            ArrayList<Double> yForces = new ArrayList<>();
            for (Planet p: planets) {
                xForces.add(p.calcNetForceExertedByX(planets));
                yForces.add(p.calcNetForceExertedByY(planets));
            }
            int i = 0;
            while (i < count){
                planets[i].update(dt, xForces.get(i), yForces.get(i));
                i += 1;
            }

            StdDraw.picture(0, 0 ,background);
            for (Planet p :planets) {
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);

            timeVar += dt;
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (Planet planet : planets) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planet.xxPos, planet.yyPos, planet.xxVel,
                    planet.yyVel, planet.mass, planet.imgFileName);
        }

    }

}
