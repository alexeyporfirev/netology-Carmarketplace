package marketplace;

import cars.Car;

public class Seller {

    private Marketplace shop;

    /**
     * Создание нового продавца, привязанного к магазину
     * @param shop Объект магазина
     */
    public Seller(Marketplace shop) {
        this.shop = shop;
    }

    /**
     * Продажа автомобиля
     */
    public void sellCar() {

            while (!Thread.currentThread().isInterrupted() && !shop.isSalesStopped()) {
                try {
                    synchronized (this) {
                        System.out.println("Покупатель " + Thread.currentThread().getName() + " зашел в магазин.");
                        while (shop.getCars().size() == 0) {
                            System.out.println("Машин нет");
                            wait();
                        }
                        System.out.println("Покупатель " + Thread.currentThread().getName() + " уехал на новеньком авто [" +
                                shop.getCars().get(0).getModel() + "].");
                        shop.getCars().remove(0);
                        shop.increaseCounter();
                        Thread.sleep(shop.getCarWaitingTime());
                    }
                } catch (InterruptedException e) {
                    System.out.println("Магазин закончил продажи!");
                }
            }
    }

    /**
     * Получение автомобиля от производителя
     */
    public void receiveCar() {
            while (!Thread.currentThread().isInterrupted() && !shop.isSalesStopped()) {
                try {
                    Thread.sleep(shop.getCarManufacturingTime());
                    synchronized (this) {
                        Car car = new Car("BMW_X5", 2022);
                        shop.getCars().add(car);
                        System.out.println("Производитель " + Thread.currentThread().getName() + " выпустил 1 авто [" +
                                car.getModel() + "].");
                        notifyAll();
                    }

                } catch (InterruptedException e) {
                    System.out.println("Магазин закончил продажи!");
                }
            }
    }
}
