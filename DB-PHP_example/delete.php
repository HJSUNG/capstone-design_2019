<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');
include('pbkdf2.compat.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
      $ID=$_POST['ID'];
      $mysqli=mysqli_connect("$host", "$username", "$password", "$dbname");

            try{
              $query_delete = "DELETE from homeseek_user WHERE ID = '".$ID."' ";
              $result = $mysqli->query($query_delete);
              $successMSG = "Successfully withdrawn !";

            } catch(PDOException $e) {
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
