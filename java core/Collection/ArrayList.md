ArrayList是list接口的可调整大小的数组实现。

ArrayList需要关注的地方有capacity,resize(即容量，和扩容)。

```
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);// 1
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
```
在代码的1处，newCapacity为oldCapacity+oldCapacity/2，如果此时新容量仍旧不满足需要的容量的最小值，那么newCapacity=minCapacity，
否则判断新容量是否大于数组允许的最大值，大于则执行hugeCapacity()：
```
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
```
hugeCapacity()使得无论如何minCapacity=min(Integer.MAX_VALUE,MAX_ARRAY_SIZE)。也即如注释所言，新的容量总数接近元素的大小。
> ArrayList最大容量为0x7fffffff