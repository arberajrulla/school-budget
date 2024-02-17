public class Student {
    private int id;
    private String name;
    private double feesPaid;

    public Student() {
    }

    public Student(int id, String name, double feesPaid) {
        this.id = id;
        this.name = name;
        this.feesPaid = feesPaid;
    }

    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public double getFeesPaid() {
        return feesPaid;
    }
    public void setFeesPaid(double feesPaid){
        this.feesPaid = feesPaid;
    }

    @Override
    public String toString() {
        return "Id=" + id +
                ", Name=" + name;
    }

    public void payFee(double amount){
        if (amount < 0){
            System.err.println("Amount to add can not be negative!");
        }else {
            feesPaid += amount;
        }
    }


}
