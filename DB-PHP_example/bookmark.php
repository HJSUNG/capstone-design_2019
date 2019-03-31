<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
      $ID=$_POST['ID'];
      $mysqli=mysqli_connect("$host", "$username", "$password", "$dbname");

            try{
              $query_search = "SELECT ID, group_concat(DISTINCT item_num) from bookmark WHERE ID = '".$ID."' group by ID";
              $result = $mysqli->query($query_search);

              if($result->num_rows == 0) {
                $errMSG = "$id,No bookmark !";
              } else {
                $row=$result->fetch_array(MYSQLI_ASSOC);
                $return_string = $row['group_concat(DISTINCT item_num)'];
                $successMSG = "$ID,$return_string";
              }
              }catch(PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
    }

?>

<?php
    if (isset($errMSG)) {
      echo $errMSG;
    } else if (isset($successMSG)) {
      echo $successMSG;
    }


    if (!$android)
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
                ID: <input type = "text" name = "ID" />
                <input type = "submit" name = "submit" />
            </form>

       </body>
    </html>

<?php
    }
?>
