public class NBody {

    public static double readRadius(String filename) {
        In in = new In(filename);     // 创建 In 对象以打开文件
        int num = in.readInt();       // 读取第一个整数（行星数量实现跳过第一行作用）并将其存储在 num 变量中
        double radius = in.readDouble();  // 读取第二个数（宇宙半径）
        return radius;                // 返回读取的宇宙半径
    }

    public static Planet[] readPlanets(String filename) {
        In in  = new In(filename);
        // 读取行星数量
        int numPlanets =in.readInt();
        // 读取半径 radius
        double radius = in.readDouble();
        // 创建 planet 数组
        Planet[] planets = new Planet[numPlanets];
        // 逐行读取行星数据并填充数组
        for (int i = 0; i < numPlanets; i++) {
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String imgFileName = in.readString();

            planets[i] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
        }
        return planets;
    }

    public static void main(String[] args) {
            double T = Double.parseDouble(args[0]);
            double dt = Double.parseDouble(args[1]);
            String filename = args[2];

            double radius = readRadius(filename);
            Planet[] planets = readPlanets(filename);

            /*-------------------- StdDraw绘图部分 --------------------*/

            /*-------- 背景绘制 --------*/
            // 设置绘图比例尺
            StdDraw.setScale(-radius, radius);
            // 在绘制新帧之前，通常需要清空画布：
            StdDraw.clear();
            // 绘制背景图像
            String backgroundImage = "images/starfield.jpg";
            StdDraw.picture(0, 0, backgroundImage);
            // 显示绘制内容
            StdDraw.show();

            /*------------------- 绘制所有行星 --------------------*/
            for (Planet p : planets) {
                p.draw();
            }
            StdDraw.show();

            /*------------------------- 创建动画 --------------------------*/
            // 启用双缓冲
            StdDraw.enableDoubleBuffering();
            // 初始化时间变量
            double t = 0.0;

            // 主循环
            while (t <= T) {
                // 创建用于存储净力的数组
                int numPlanets = planets.length;
                double[] xForces = new double[numPlanets];
                double[] yForces = new double[numPlanets];

                // 计算每个行星的净力
                for (int i = 0; i < numPlanets; i++) {
                    xForces[i] = planets[i].calcNetForceExertedByX(planets);
                    yForces[i] = planets[i].calcNetForceExertedByY(planets);
                }

                // 更新每个行星的状态
                for (int i = 0; i < numPlanets; i++) {
                    planets[i].update(dt, xForces[i], yForces[i]);
                }

                // 绘制背景
                StdDraw.clear();
                StdDraw.picture(0, 0, "images/starfield.jpg");

                // 绘制所有行星
                for (Planet p : planets) {
                    p.draw();
                }

                // 显示绘制内容
                StdDraw.show();

                // 暂停动画
                StdDraw.pause(10);

                // 增加时间变量
                t += dt;
            }



            StdOut.printf("%d\n", planets.length);
            StdOut.printf("%.2e\n", radius);
            for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
            }


    }

}
