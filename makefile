SOURCE = src/
BUILD = build/
RESOURCE = resources/
MAIN_CLASS = view.Gui

compile: clean
		@mkdir -p $(BUILD)
		@javac -sourcepath $(SOURCE) -d $(BUILD) $(SOURCE)*/*.java
		@echo "Compilation completed"

run: compile
		@java -cp $(BUILD) $(MAIN_CLASS)

clean:
		@rm -rf $(BUILD)