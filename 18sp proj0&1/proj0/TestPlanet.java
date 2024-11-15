public class TestPlanet {

    public static void main(String[] args) {
        // 查创建两个行星对象
        Planet planet1 = new Planet(1.0, 2.0, 3.0, 4.0, 5.0, "planet1.jpg");
        Planet planet2 = new Planet(4.0, 6.0, -3.0, -4.0, 10.0, "planet2.jpg");

        // 计算并打印两个行星之间的成对引力
        double force = planet1.calcForceExertedBy(planet2);
        System.out.println("The force exerted by planet2 on planet1 is: " + force + " N");

        // 进一步测试 X 和 Y 方向的引力分量
        double forceX = planet1.calcForceExertedByX(planet2);
        double forceY = planet1.calcForceExertedByY(planet2);

        System.out.println("The X component of the force exerted by planet2 on planet1 is: " + forceX + " N");
        System.out.println("The Y component of the force exerted by planet2 on planet1 is: " + forceY + " N");

    }
}
