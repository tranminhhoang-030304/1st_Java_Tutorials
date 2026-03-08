import java.util.Arrays;
public class ArrayAlgorithms {
    public static void main(String[] args) {
        int[] numbers = {5, 2, 9, 1, 6};
        System.out.println("Mảng ban đầu: " + Arrays.toString(numbers));

        // 1. THÊM PHẦN TỬ
        int[] afterAdd = addElement(numbers, 99, 2); // Thêm số 99 vào vị trí index 2
        System.out.println("Sau khi thêm 99 vào index 2: " + Arrays.toString(afterAdd));

        // 2. XÓA PHẦN TỬ
        int[] afterRemove = removeElement(numbers, 1); // Xóa phần tử ở index 1 (số 2)
        System.out.println("Sau khi xóa phần tử ở index 1: " + Arrays.toString(afterRemove));

        // 3. Bubble Sort
        int[] arrayToBubbleSort = {8, 3, 7, 4, 2};
        bubbleSort(arrayToBubbleSort);
        System.out.println("Sau khi Bubble Sort: " + Arrays.toString(arrayToBubbleSort));

        // 4. Selection Sort
        int[] arrayToSelectionSort = {12, 5, 3, 9, 1};
        selectionSort(arrayToSelectionSort);
        System.out.println("Sau khi Selection Sort: " + Arrays.toString(arrayToSelectionSort));
    }

    // --- Thuật toán THÊM 1 phần tử vào mảng ---
    public static int[] addElement(int[] original, int element, int index) {
        // Tạo mảng mới có kích thước lớn hơn 1
        int[] result = new int[original.length + 1];
        for (int i = 0, j = 0; i < result.length; i++) {
            if (i == index) {
                result[i] = element; // Chèn phần tử mới
            } else {
                result[i] = original[j++]; // Copy phần tử cũ sang
            }
        }
        return result;
    }

    // --- Thuật toán XÓA 1 phần tử khỏi mảng ---
    public static int[] removeElement(int[] original, int index) {
        // Nếu mảng rỗng hoặc index không hợp lệ, trả về mảng cũ
        if (original == null || index < 0 || index >= original.length) {
            return original;
        }
        // Tạo mảng mới có kích thước nhỏ hơn 1
        int[] result = new int[original.length - 1];
        for (int i = 0, j = 0; i < original.length; i++) {
            if (i == index) {
                continue; // Bỏ qua phần tử cần xóa
            }
            result[j++] = original[i]; // Copy các phần tử còn lại
        }
        return result;
    }

    // --- Thuật toán SẮP XẾP: Bubble Sort (Nổi bọt) ---
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // Đổi chỗ (Swap) nếu phần tử trước lớn hơn phần tử sau
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // --- Thuật toán SẮP XẾP: Selection Sort (Sắp xếp chọn) ---
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i; // Giả sử phần tử hiện tại là nhỏ nhất
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j; // Cập nhật lại vị trí nhỏ nhất
                }
            }
            // Đổi chỗ phần tử nhỏ nhất tìm được với phần tử ở vị trí i
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
    }
}