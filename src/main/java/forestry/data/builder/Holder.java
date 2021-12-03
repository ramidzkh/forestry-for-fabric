package forestry.data.builder;

class Holder<T> {
    private T object;

    public T get() {
        if (object == null) {
            throw new IllegalStateException("No value was returned during the creation of the recipe..");
        }

        return object;
    }

    public void set(T object) {
        this.object = object;
    }
}
