public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV,
                  double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;

    }


    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        return Math.sqrt((xxPos - p.xxPos) * (xxPos - p.xxPos)
                + (yyPos - p.yyPos) * (yyPos - p.yyPos));
    }

    public double calcForceExertedBy(Planet p) {
        double r = calcDistance(p);
        return G * (mass * p.mass) / (r * r);
    }

    public double calcForceExertedByX(Planet p) {
        double dx = p.xxPos - xxPos;
        double r = calcDistance(p);
        return  calcForceExertedBy(p) * dx / r;
    }

    public double calcForceExertedByY(Planet p) {
        double dy = p.yyPos - yyPos;
        double r = calcDistance(p);
        return calcForceExertedBy(p) * dy / r;
    }

    public double calcNetForceExertedByX(Planet[] allPlanets) {
        double netForceX = 0.0;
        for (Planet p : allPlanets) {
            if (this.equals(p)) {
                continue; // 忽略自身
            }
            netForceX += this.calcForceExertedByX(p); // 累加每个行星在 X 方向上的引力
        }
        return netForceX;
    }

    public double calcNetForceExertedByY(Planet[] allPlanets) {
        double netForceY = 0.0;
        for (Planet p : allPlanets) {
            if (this.equals(p)) {
                continue;
            }
            netForceY += this.calcForceExertedByY(p);
        }
        return netForceY;
    }

    /* 根据施加在行星上的力和时间步长，更新行星的速度和位置 */
    public void update(double dt, double fX, double fY) {
        // 计算加速度
        double aX = fX / this.mass;
        double aY = fY / this.mass;

        // 更新速度
        this.xxVel += aX * dt;
        this.yyVel += aY * dt;

        // 更新位置
        this.xxPos += this.xxVel * dt;
        this.yyPos += this.yyVel * dt;
    }

    /* 绘制一颗行星 */
    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }

}


