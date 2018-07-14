package edu.uci.ZotFinder.data;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import edu.uci.ZotFinder.R;
import edu.uci.ZotFinder.activity.SearchActivity;


public class SearchPersonTask extends AsyncTask<String, Void, List<HashMap<String, String>>> {

    private final SearchActivity searchActivity;
    private final ListView searchResults;

    public SearchPersonTask(SearchActivity searchActivity, ListView searchResults) {
        this.searchActivity = searchActivity;
        this.searchResults = searchResults;
    }

    protected List<HashMap<String, String>> doInBackground(String... input) {
        try {
            return personSearchResultType(input[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //return false = list of people
    //return true = one person
    public List<HashMap<String, String>> personSearchResultType(String inputValue) throws InterruptedException, ExecutionException {
        String input = inputValue;
        String url = "https://directory.uci.edu/index.php?basic_keywords=" + Uri.encode(input.replace(" ", "+"), "+") + "&modifier=Starts+With&basic_submit=Search&checkbox_employees=Employees&form_type=basic_search";
        String output = searchActivity.retrieveDirectoryResult(url);
        if(output.contains("Your search"))
            return readMultipleResultStream(output, inputValue);
        else
            return searchActivity.readSingleResultStream(output, input);
    }

    private List<HashMap<String, String>> readMultipleResultStream(String output, String input) throws InterruptedException, ExecutionException {
        List<HashMap<String, String>> personResults = new ArrayList<HashMap<String, String>>();

        String[] ucinetidSplit = output.split("uid=");
        String[] nameSplit = output.split("&return=basic_keywords%3D" + Uri.encode(input.replace(" ", "+")) + "%26modifier%3DStarts%2BWith%26basic_submit%3DSearch%26checkbox_employees%3DEmployees%26form_type%3Dbasic_search'>");
        String[] titleSplit = output.split("<span class=\"departmentmajor\">");
        int j = 1;
        for(int i = 1; i < nameSplit.length; i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("personid", "" +i);
            map.put("ucinetid", ucinetidSplit[i].split("&")[0]);
            map.put("name", nameSplit[i].split("</a>")[0]);
            if(titleSplit[j].split("</span>")[0].contains("<br />"))
                map.put("title",titleSplit[j].split("</span>")[0].split("<br />")[0]);
            else
                map.put("title",titleSplit[j].split("</span>")[0]);
            j += 2;
            personResults.add(map);
        }
        return personResults;
    }
    protected void onPostExecute(List<HashMap<String, String>> result) {
        String[] from = new String[] {"name", "title"};
        int[] to = new int[] {R.id.personName, R.id.personTitle};
        SimpleAdapter simpleAdapter = new SimpleAdapter(searchActivity,result, R.layout.activity_person_list_item,from,to);
        if(!isCancelled()) {
            searchActivity.stopProgress(simpleAdapter);
            if (result != null && !result.isEmpty()) {
                searchResults.setAdapter(simpleAdapter);
            } else {
                Toast.makeText(searchActivity, "No Results Found. Please Try Again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
