package System.Stock;

public class StockPurchaseDetails {
        private int quantity;
        private double purchasePrice;

        public StockPurchaseDetails(int quantity, double purchasePrice) {
            this.quantity = quantity;
            this.purchasePrice = purchasePrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPurchasePrice() {
            return purchasePrice;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setPurchasePrice(double purchasePrice) {
            this.purchasePrice = purchasePrice;
        }

}
