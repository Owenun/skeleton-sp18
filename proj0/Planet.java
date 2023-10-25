public class Planet {
    double xxPos;
    double yyPos;
    double xxVel;
    double yyVel;
    double mass;
    String imgFileName;

    public Planet(double xp, double yp, double xV, double yV, double m, String img) {
        xxPos = xp;
        yyPos = yp;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;

    }

    public Planet(Planet p){
        this(p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);

    }

    public double calcDistance(Planet p) {
        double absX = this.xxPos - p.xxPos;
        double absY = this.yyPos - p.yyPos;

        return Math.sqrt(absX * absX + absY * absY);

    }

    public double calcForceExertedBy(Planet p) {
        if (p.equals(this)) throw new RuntimeException();

        final double G = 6.67 * Math.pow(10, -11);

        double distance = this.calcDistance(p);
        return (this.mass * p.mass / (distance * distance)) * G;

    }

    public double calcForceExertedByX(Planet p) {

        double xDistance = -this.xxPos + p.xxPos;
        double distance = this.calcDistance(p);
        double force = calcForceExertedBy(p);
        return  (xDistance / distance) * force;




    }
    public double calcForceExertedByY(Planet p) {
        double yDistance = -this.yyPos + p.yyPos;
        double distance = this.calcDistance(p);
        double force = calcForceExertedBy(p);
        return  (yDistance / distance) * force;
    }

    public double calcNetForceExertedByX(Planet[] planets) {
        double netForceX = 0;
        for (Planet p: planets) {
            netForceX += this.calcForceExertedByX(p);
        }
        return netForceX;
    }
    public double calcNetForceExertedByY(Planet[] planets) {
        double netForceY = 0;
        for (Planet p: planets) {
            netForceY += this.calcForceExertedByY(p);
        }
        return netForceY;
    }


    public void update(double dt, double forceX, double forceY) {

        double ax = forceX / this.mass;
        double ay = forceY / this.mass;

        this.xxVel = ax * dt + this.xxVel;
        this.yyVel = ay * dt + this.yyVel;

        this.xxPos += this.xxVel * dt;
        this.yyPos += this.yyVel * dt;

    }





















}
