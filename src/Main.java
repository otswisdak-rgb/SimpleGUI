import java.util.ArrayList;

//Main method.  Supposed to run everything
public class Main
{
    public static void main(String[] args)
    {
        App app = new App("My tasks");
        Page dashboardPage.addPage("Dashboard");
        Page tasksPage.addPage("Tasks");
        Page addTaskPage.addPage("Add task");

        ArrayList tasks = new ArrayList();

        AddTaskHandler addTaskHandler = new AddTaskHandler(tasks);

        String[] columnNames = {"Date", "Description", "Type", "Category", "Calories"};
        Table allTasksTable = new Table(columnNames, tasks);

        tasksPage.addComponent(allTasksTable);

        Form addTaskForm = new Form(columnNames, "Add Task", addTaskHandler);
        addTaskPage.addComponent(addTaskForm);
        

        app.show();
    }
}
class Task implements Rowable
{
    String date;
    String description;
    String type;
    String category;
    int calories;

    public Task(String date, String description, String type, String category, int calories);

    @Override
    public Object[] getRowData()
    {
        String[] rowData = {date, description, type, category,String.valueOf(calories)};
        return rowData;
    }
}
class AddEntryHandler implements FormSubmitHandler
{
    ArrayList objects;

    public AddEntryHandler(ArrayList objects)
    {
        this.objects = objects;
    }
        @Override
        public void onSubmit(String[] values)
        {
            Task task = new Task (values[0], values[1], values[2], values[3], values[4]);
            objects.add(task);
        }
}

//Dashboard class. Supposed to show a panel with 3 statcards, and 2 charts
    //1. Net calories(Meal calories - Exercise calores)
    //2. Calories eaten(How many calories in each meal)
    //3. Calories burned(How many calories exercised)
    //4. Pie Chart(Shows how many calories eaten by meal)
    //5. Bar Chart(Show how many calories burned by specific exercise)
class Dashboard
{
    Table tasks;
    Page dashboardPage;
    Panel APanel, BPanel, CPanel;
    public Dashboard(Table tasks, Page dashboardPage)
    {
        this.tasks = tasks;
        this.dashboardPage = dashboardPage;
        APanel = new Panel(rows: 2, cols: 1, hgap: 10, vgap: 10);
        BPanel = new Panel(rows: 1, cols: 3, hgap: 10, vgap: 10);
        CPanel = new Panel(rows: 1, cols: 2, hgap: 10, vgap: 10);
        APanel.add(BPanel);
        APanel.add(CPanel);
    }

    public void update()
    {
        StatCard netCalories = new StatCard("Total amount of calories", 
        String.valueOf(tasks.getRowCount()));

        StatCard totalCaloriesEaten = new StatCard("Total calories eaten",
        String.valueOf(tasks.getRowCount()));

        StatCard totalCaloriesBurned = new StatCard("Total calories burned",
        String.valueOf(tasks.getRowCount()));

        PieChart chart = new PieChartBuilder().width(10).height(10).title("Calories eaten");
        chart.addSeries("Breakfast");
        chart.addSeries("Lunch");
        chart.addSeries("Dinner");
        chart.addSeries("Snack");

        BarChart chart = new BarChartBuilder().width(10).height(10).title("Calories burned");
        chart.addSeries("Running");
        chart.addSeries("Biking");
        chart.addSeries("Walking");
        chart.addSeries("Lifting");
    }
}
class Model
{
    ArrayList tasks;
    void add(Task task)
    {
        tasks.add(task);
    }
    

}
class View
{
    Page tasksPage, addTasksPage, dashboardPage;
    Table allTasksTable;
    Model model;
    AddTaskHandler addTaskHandler;
    Form addTaskForm;
    public view(App app, Model model)
    {
      
    }
    void update()
    {
        //1. Update table
        //2. Update stat cards
    }
}



