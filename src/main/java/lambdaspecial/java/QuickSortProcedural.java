package lambdaspecial.java;

import java.util.ArrayList;
import java.util.List;

public class QuickSortProcedural<T extends Comparable<T>> implements Sort<T> {

    @Override
    public List<T> sort(List<T> array) {
        if (array == null) {
            return null;
        }
        // 원본 리스트를 변경하지 않기 위해 복사본을 만들어 사용
        List<T> listToSort = new ArrayList<>(array);
        quickSortRecursive(listToSort, 0, listToSort.size() - 1);
        return listToSort;
    }

    private void quickSortRecursive(List<T> list, int low, int high) {
        if (low < high) {
            // 파티션하고 피벗의 최종 위치를 얻음
            int pivotIndex = partition(list, low, high);

            // 피벗을 기준으로 좌우 부분 리스트를 재귀적으로 정렬
            quickSortRecursive(list, low, pivotIndex - 1);
            quickSortRecursive(list, pivotIndex + 1, high);
        }
    }

    private int partition(List<T> list, int low, int high) {

        int mid = low + (high - low) / 2;
        T pivot = list.get(mid);
        swap(list, mid, high); // 중간 피벗을 마지막으로 옮겨서 아래 로직과 호환

        int i = low - 1; // 작은 요소들의 마지막 위치를 가리키는 인덱스

        for (int j = low; j < high; j++) {
            // 현재 요소가 피벗보다 작거나 같으면
            if (list.get(j).compareTo(pivot) <= 0) {
                i++;
                swap(list, i, j); // 작은 요소를 왼쪽으로 이동
            }
        }

        // 피벗을 올바른 위치로 이동 (i + 1)
        // (i + 1) 위치에는 피벗보다 큰 첫 번째 요소가 있거나, 모든 요소가 피벗보다 작거나 같았다면 high 위치
        swap(list, i + 1, high);
        return i + 1; // 피벗의 최종 위치 반환
    }

    private void swap(List<T> list, int i, int j) {
        if (i == j) return; // 같은 인덱스는 바꿀 필요 없음
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    /*
     * int[] 배열을 정렬하는 메서드
     */

    public void sort(int[] array) {
        if (array == null || array.length < 2) {
            return;
        }
        quickSortRecursive(array, 0, array.length - 1);
    }

    private void quickSortRecursive(int[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSortRecursive(array, low, pivotIndex - 1);
            quickSortRecursive(array, pivotIndex + 1, high);
        }
    }

    private int partition(int[] array, int low, int high) {
        // 랜덤 피벗 선택
        int mid = low + (high - low) / 2;
        int pivot = array[mid];
        swap(array, mid, high); // 피벗을 맨 뒤로 이동

        int i = low - 1; // 작은 요소들의 마지막 위치

        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i + 1, high); // 피벗을 최종 위치로 이동
        return i + 1; // 피벗의 최종 위치 반환
    }

    private void swap(int[] array, int i, int j) {
        if (i == j) return;
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
