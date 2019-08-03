<?php 

    class DbOperations {
        
        private $con;
        
        function __construct(){
            require_once dirname (__FILE__) . '/db-connect.php';
            
            $db = new DbConnect;
            $this->con = $db->connect();
        }
        
        public function createUser($email, $password, $name, $school){
            if(!$this->isEmailExist($email)){
                $statement = $this->con->prepare('INSERT INTO users (email, password, name, school) VALUES (?, ?, ?, ?)');
                $statement->bind_param('ssss', $email, $password, $name, $school);
                
                if($statement->execute()){
                    return USER_CREATED;
                } else {
                    return USER_FAILURE;
                }   
            }
            return USER_EXISTS;
        }
        
        public function userLogin($email, $password) {
            if($this->isEmailExist($email)){
                $hashed_password = $this->getUsersPasswordByEmail($email);
                if(password_verify($password, $hashed_password)){
                   return USER_AUTHENTICATED;
                } else {
                    return USER_PASSWORD_DO_NOT_MATCH;
                }
            } else {
                return USER_NOT_FOUND;
            }
        }
        
        private function getUsersPasswordByEmail($email){
            $statement = $this->con->prepare('SELECT password FROM users WHERE email = ?');
            $statement->bind_param('s', $email);
            $statement->execute();
            $statement->bind_result($password);
            $statement->fetch();
            return $password;
        }
        
        public function getAllUsers(){
            $statement = $this->con->prepare('SELECT id, email, name, school FROM users;');
            $statement->execute();
            $statement->bind_result($id, $email, $name, $school);
            $users = array();
            
            while($statement->fetch()){
                $user = array();
                $user['id'] = $id;
                $user['email'] = $email;
                $user['name'] = $name;
                $user['school'] = $school;
                
                array_push($users, $user);
        
            }
            return $users;
            
        }
        
        public function getUserByEmail($email){
            $statement = $this->con->prepare('SELECT id, email, name, school FROM users WHERE email = ?');
            $statement->bind_param('s', $email);
            $statement->execute();
            $statement->bind_result($id, $email, $name, $school);
            $statement->fetch();
            
            $user = array();
            $user['id'] = $id;
            $user['email'] = $email;
            $user['name'] = $name;
            $user['school'] = $school;
            
            return $user;
        }
        
        public function updateUser($email, $name, $school, $id){
            $statement = $this->con->prepare('UPDATE users SET email = ?, name = ?, school = ? WHERE id = ?');
            $statement->bind_param('sssi', $email, $name, $school, $id);
            if($statement->execute())
                return true;
            return false;
            
        }
        
        public function updatePassword($current_password, $new_password, $email){
            $hashed_password = $this->getUsersPasswordByEmail($email);
            
            if(password_verify($current_password, $hashed_password)){
                
                $hash_password = password_hash($new_password, PASSWORD_DEFAULT);
                
                $statement = $this->con->prepare('UPDATE users SET password = ? WHERE email = ?');
                $statement->bind_param('ss', $hash_password, $email);
                
                if($statement->execute())
                    return PASSWORD_CHANGED;
                return PASSWORD_NOT_CHANGED;
                
                
            } else {
                return PASSWORD_DO_NOT_MATCH;
            }
        }
        
        public function deleteUser($id){
            $statement = $this->con->prepare('DELETE FROM users WHERE id = ?');
            $statement->bind_param('i', $id);
            if($statement->execute())
                return true;
            return false;
        }
        
        private function isEmailExist($email){
            $statement = $this->con->prepare('SELECT id FROM users WHERE email = ?');
            $statement->bind_param('s', $email);
            $statement->execute();
            $statement->store_result();
            return $statement -> num_rows > 0;
        }
        
    }