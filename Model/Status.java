// package Model;

// public enum Status {
//     PENDING("pending"),
//     IN_PROGRESS("in_progress"),
//     ON_DELIVERY("on_delivery"),
//     ARRIVED("arrived");

//     private final String status;

//     Status(String status) {
//         this.status = status;
//     }

//     public String getStatus() {
//         return status;
//     }

//     public static Status fromString(String status) {
//         for (Status s : Status.values()) {
//             if (s.status.equalsIgnoreCase(status)) {
//                 return s;
//             }
//         }
//         throw new IllegalArgumentException("Error: " + status);
//     }
// }
