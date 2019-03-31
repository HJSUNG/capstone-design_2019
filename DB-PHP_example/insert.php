<?php

    error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon.php');
    include('pbkdf2.compat.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        $ID=$_POST['ID'];
        $PW=$_POST['PW'];
        $nickname=$_POST['nickname'];
        $user_type=$_POST['user_type'];
        $phone=$_POST['phone'];

        $hash = create_hash($PW);


        if(empty($ID)){
            $errMSG = "Input ID.";
        }
        else if(empty($PW)){
            $errMSG = "Input Password.";
        }
        else if(empty($nickname)){
            $errMSG = "Input Nickname.";
        }
        else if(empty($phone)){
            $errMSG = "Input Nickname.";
        }

        if(!isset($errMSG))

        {
            try{

                $stmt = $con->prepare('INSERT INTO homeseek_user(ID, PW, nickname, user_type, phone) VALUES(:ID, :PW, :nickname, :user_type, :phone)');
                $stmt->bindParam(':ID', $ID);
                $stmt->bindParam(':PW', $PW);
                $PW = $hash;
                $stmt->bindParam(':nickname', $nickname);
                $stmt->bindParam(':user_type', $user_type);
                $stmt->bindParam(':phone', $phone);

                if($stmt->execute())
                {
                    $successMSG = "New User Added !";
                }
                else
                {
                    $errMSG = "ERROR";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
        }

    }

?>


<?php
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;


    if (!$android)
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
                ID: <input type = "text" name = "ID" />
                PW: <input type = "text" name = "PW" />
                nickname: <input type = "text" name = "nickname" />
                user_type: <input type = "text" name = "user_type"/>
                phone: <input type= "text" name = "phone"/>
                <input type = "submit" name = "submit" />
            </form>

       </body>
    </html>

<?php
    }
?>
